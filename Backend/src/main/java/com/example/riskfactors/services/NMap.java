package com.example.riskfactors.services;

import com.example.riskfactors.utils.RunProcess;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NMap implements EnrichmentTools {

    // @Value("${TEMP-JSON}")
    private String tempJson = "temp.json";
    private String path;
    private String OS;

    public void setPath(String path) {
        this.path = path;
    }

    public String operatingSystem() {
        if (this.OS == null) {
            this.OS = System.getProperty("os.name");
        }
        return this.OS;
    }

    public boolean isWindows() // check if OS is Windows or not
    {
        return operatingSystem().startsWith("Windows");
    }

    void runProcess(String comm, String url, String check) throws IOException, InterruptedException {
        String command = comm + " " + url + " -oX " + url + check + ".xml";
        log.info("Running command: " + command);
        RunProcess runProcess = new RunProcess();
        runProcess.runProcess(command);
    }

    /*check if the web app has the following headers: csp, x-content-type-options*/
    @Override
    public List<String> scanApplication(String url) throws IOException, InterruptedException {
        ArrayList<String> issues = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>();
        headers.add("X_Frame_Options");
        headers.add("X_Content_Type_Options");
        headers.add("Content-Security-Policy");
        runProcess("nmap --script http-security-headers", url, "headers");
        ArrayList<String> httpHeadersFound;
        if (isWindows()) {
            convertToJSON(path + "\\" + url + "headers");
            httpHeadersFound = parseJSONReport(path + "\\" + tempJson, "header");
        } else { //linux
            convertToJSON(url + "headers");
            httpHeadersFound =  parseJSONReport(tempJson, "header");
            //System.out.println("headerrr");
        }
        log.info("Headers found:");
        for (int i = 0; i < headers.size(); i++) {
            if (!httpHeadersFound.contains(headers.get(i)))
                issues.add(headers.get(i));
        }

//        checking for cookie flags
        runProcess("nmap --script http-cookie-flags", url, "cookies");
        if (isWindows()) {
            convertToJSON(path + "\\" + url + "cookies");

        } else { //linux
            convertToJSON(url + "cookies");
        }
//        make the if

        // convertToJSON(url + "cookies");
        ArrayList<String> missingCookieHeaders = parseJSONReport(tempJson, "cookies");
        log.info("Missing cookie flags:");
        issues.addAll(missingCookieHeaders);
//        checking for csrf vulnerabilities
        runProcess("nmap --script http-csrf.nse", url, "csrf");
        convertToJSON(url + "csrf");
        ArrayList<String> csrfVulnerabilities = parseJSONReport(tempJson, "csrf");
        log.info("Total CSRF Vulnerabilities:" + csrfVulnerabilities.size());
        // log.info(String.valueOf(csrfVulnerabilities));

        if (!csrfVulnerabilities.isEmpty()) {
            issues.add("csrf");
        }

        return issues;

    }

    @Override
    public void convertToJSON(String name) throws IOException, InterruptedException {
        JSONObject xmlJSONObj;
        String TEST_XML_STRING = null;
        FileInputStream fin = null;
        File filew;
        FileWriter wfile = null;
        try {
            File file = new File(name + ".xml");
            fin = new FileInputStream(file);
            byte[] xmlData = new byte[(int) file.length()];
            fin.read(xmlData);
            fin.close();
            TEST_XML_STRING = new String(xmlData, StandardCharsets.UTF_8);

        } catch (Exception ex) {
            log.error("1- Exception in Kafka NMap->convertToJSON Method :{} ", ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                fin.close();
            } catch (NullPointerException e) {
                log.error("Error In closing File Input Stream");
            }
        }
        try {
            xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);
            filew = new File("temp.json");
            filew.createNewFile();
            wfile = new FileWriter(filew);
            wfile.write(xmlJSONObj.toString(4));

        } catch (Exception e) {
            log.error("2- Exception in Kafka NMap->convertToJSON Method :{} ", e.getMessage());
            e.printStackTrace();

        } finally {
            try {
                wfile.flush();
                wfile.close();
            } catch (Exception e) {
                log.error("3- Exception in Kafka NMap->convertToJSON Method :{} ", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<String> parseJSONReport(String path, String check) throws IOException {
        JSONParser jsonParser = new JSONParser();
        JSONObject object = new JSONObject();

        try (FileReader reader = new FileReader(path)) {
            Object obj = jsonParser.parse(reader);
            object = new JSONObject((Map) obj);
        } catch (FileNotFoundException | ParseException e) {
            log.error("Exception in Kafka NMap->parseJSONReport Method :{} ", e.getMessage());
        }
        ArrayList<String> headers = new ArrayList<>();

        if (object.has("nmaprun")) {
            JSONObject nmaprun = object.getJSONObject("nmaprun");
            if (nmaprun.has("host")) {
                JSONObject host = nmaprun.getJSONObject("host");
                if (host.has("ports")) {
                    JSONObject ports = host.getJSONObject("ports");
                    if (ports.has("port")) {
                        JSONArray portDetails = ports.getJSONArray("port");
                        if (check.equals("csrf")) {
                            for (int i = 0; i < portDetails.length(); i++) {
                                JSONObject obj = portDetails.getJSONObject(i);
                                if (obj.has("script")) {
                                    JSONObject script = obj.getJSONObject("script");
                                    if (script.has("output")) {
                                        String output = script.getString("output");
                                        if (output.contains("Found the following possible CSRF vulnerabilities")) {
                                            //counting the vulnerabilities
                                            String[] splits = output.split("Path: ");
                                            for (int j = 0; j < splits.length; j++) {
                                                if (splits[i].contains("Form id:")) {
                                                    headers.add("CSRF " + splits[i]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (portDetails.length() >= 2) {
                                JSONObject portDetailsObj = portDetails.getJSONObject(1);
                                if (portDetailsObj.has("script")) {
                                    JSONObject script = portDetailsObj.getJSONObject("script");
                                    if (script.has("table")) {
                                        Object item = script.get("table");
                                        if (item instanceof JSONArray) {
                                            JSONArray table = (JSONArray) item;
                                            for (int i = 0; i < table.length(); i++) {
                                                JSONObject infoObject = table.getJSONObject(i);
                                                if (check.equals("header") && infoObject.has("key")) {

                                                    headers.add(infoObject.getString("key"));
                                                }
                                                if (check.equals("cookies")) {
                                                    if (infoObject.has("elem")) {
                                                        String elem = infoObject.getString("elem");
                                                        if (elem.contains("secure flag"))
                                                            headers.add("secure");
                                                        if (elem.contains("httponly"))
                                                            headers.add("httponly");
                                                    } else {
                                                        if (infoObject.has("table")) {
                                                            JSONObject table2 = infoObject.getJSONObject("table");
                                                            if (table2.has("elem")) {
                                                                String elem = table2.getString("elem");
                                                                if (elem.contains("secure flag"))
                                                                    headers.add("secure");
                                                                if (elem.contains("httponly"))
                                                                    headers.add("httponly");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            JSONObject table = (JSONObject) item;
                                            if (check.equals("header")) {
                                                if (table.has("key"))
                                                    headers.add(table.getString("key"));
                                            }
                                            if (check.equals("cookies")) {
                                                if (table.has("table")) {
                                                    JSONObject table2 = table.getJSONObject("table");
                                                    if (table2.has("elem")) {
                                                        String elem = table2.getString("elem");
                                                        if (elem.contains("secure flag"))
                                                            headers.add("secure");
                                                        if (elem.contains("httponly"))
                                                            headers.add("httponly");
                                                    }
                                                } else {
                                                    if (table.has("elem")) {
                                                        String elem = table.getString("elem");
                                                        if (elem.contains("secure flag"))
                                                            headers.add("secure");
                                                        if (elem.contains("httponly"))
                                                            headers.add("httponly");
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }

                            }
                        }

                    }
                }
            }
        }

        return headers;
    }
}
