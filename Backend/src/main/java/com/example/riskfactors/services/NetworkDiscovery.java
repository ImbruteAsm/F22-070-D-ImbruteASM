package com.example.riskfactors.services;

import com.example.riskfactors.model.*;
import com.example.riskfactors.utils.Constants;
import com.example.riskfactors.utils.Parser;
import com.example.riskfactors.utils.RunProcess;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class NetworkDiscovery {


    public List<Subdomain> discoverSubDomains(String target) {
        log.info("Discovering Subdomains");
        String cmdTemplate = Constants.SUBDOMAINS_CMD;
        String fileName = this.makeFile("subdomains", "xml");
        RunProcess process = new RunProcess();
        String subdomainCmd = String.format(cmdTemplate, target, fileName);
        Boolean isFinished = process.runProcess(subdomainCmd);
        if (isFinished.equals(Boolean.TRUE)) {
            Parser converter = new Parser();
            try {
                String jsonString = converter.convert(fileName);
                return this.parseSubdomains(jsonString);
            } catch (Exception e) {
                log.error("Exception in Kafka NetworkDiscovery->discoverSubDomains Method :{} ", e.getMessage());
            }
        }
        return null;
    }

    private List<Subdomain> parseSubdomains(String jsonString) {
        log.info("Parsing Subdomains");
        List<Subdomain> subdomains = new ArrayList<>();
        JSONObject data = new JSONObject(jsonString);
        log.info(data.toString());
        boolean isThere = false;
        try {
            isThere = data.getJSONObject("nmaprun").getJSONObject("host").getJSONObject("hostscript").getJSONObject("script").getJSONObject("table").has("table");
        } catch (Exception e) {
            log.error("Exception in Kafka NetworkDiscovery->parseSubdomains Method :{} ", e.getMessage());
        }
        if (isThere) {
            Object object = data.getJSONObject("nmaprun").getJSONObject("host").getJSONObject("hostscript").getJSONObject("script").getJSONObject("table").get("table");
            if (object instanceof JSONObject) {
            } else {
                JSONArray tableOfContents = data.getJSONObject("nmaprun").getJSONObject("host").getJSONObject("hostscript").getJSONObject("script").getJSONObject("table").getJSONArray("table");
                for (int i = 0; i < tableOfContents.length(); i++) {
                    Subdomain subdomain = new Subdomain();
                    JSONObject element = tableOfContents.getJSONObject(i);
                    JSONArray contents = element.getJSONArray("elem");
                    for (int j = 0; j < contents.length(); j++) {
                        JSONObject dataOfTable = contents.getJSONObject(j);
                        if (dataOfTable.getString("key").equals("hostname")) {
                            subdomain.setUrl(dataOfTable.getString("content"));
                        } else {
                            subdomain.setIp(dataOfTable.getString("content"));
                        }
                    }
                    subdomains.add(subdomain);
                }
            }
            return subdomains;
        }
        return null;
    }


    

    public ServiceFactors discoverServices(String target) throws IOException {
        log.info("Discovering Services");
        Map<String, Boolean> services = new HashMap<>();
        List<Port> ports = new ArrayList<>();
        Enumeration<String> scripts = Collections.enumeration(new ArrayList<>(Constants.SERVICES_CMD.keySet()));
        while (scripts.hasMoreElements()) {
            String serviceName = scripts.nextElement();
            if (!serviceName.equals("ssl") && !serviceName.equals("ssh") && !serviceName.equals("subdomains")) {
                String cmd = Constants.SERVICES_CMD.get(serviceName);
                String fileName = this.makeFile(serviceName, "xml");
                RunProcess process = new RunProcess();
                String nmapCmd = cmd + fileName + " " + target + " >/dev/null";
                Boolean isFinished = process.runProcess(nmapCmd);
                Boolean isDiscovered = false;
                if (isFinished.equals(Boolean.TRUE)) {
                    Parser converter = new Parser();
                    String jsonString = converter.convert(fileName);
                    if (serviceName.equals("rsync") || serviceName.equals("rdp")) {
                        isDiscovered = this.parseNmapCmd(jsonString);
                        services.put(serviceName, isDiscovered);
                    } else if (serviceName.equals("dns")) {
                        log.info("IN DNS");
                        Map<String, Boolean> dnsServices = this.parseDNS(jsonString);
                        ports = this.parsePorts(jsonString);
                        if (dnsServices != null) {
                            log.info(String.valueOf(dnsServices));
                            services.putAll(dnsServices);

                        }
                    } else {
                        isDiscovered = this.parseScript(jsonString);
                        services.put(serviceName, isDiscovered);
                    }
                }
            }
        }
        ServiceFactors serviceFactors = new ServiceFactors();
        serviceFactors.setServiceDict(services);
        serviceFactors = initServiceFactors(serviceFactors);
        serviceFactors.setPorts(ports);
        return serviceFactors;
    }

    public ServiceFactors initServiceFactors(ServiceFactors serviceFactors) {
        log.info("Initializing Service Factors");
        for (Map.Entry<String, Boolean> service : serviceFactors.getServiceDict().entrySet()) {
            String name = service.getKey();
            switch (name) {
                case "mongo":
                    serviceFactors.setMongoDiscovered(service.getValue());
                    break;
                case "cassandra":
                    serviceFactors.setCassandraDiscovered(service.getValue());
                    break;
                case "microsoftSQL":
                    serviceFactors.setMsSQLDiscovered(service.getValue());
                    break;
                case "mySQL":
                    serviceFactors.setMySQLDiscovered(service.getValue());
                    break;
                case "redis":
                    serviceFactors.setRedisDiscovered(service.getValue());
                    break;
                case "rsync":
                    serviceFactors.setRsyncDiscovered(service.getValue());
                    break;
                case "rdp":
                    serviceFactors.setRdpDiscovered(service.getValue());
                    break;
                case "vnc":
                    serviceFactors.setVncDiscovered(service.getValue());
                    break;
                case "ftp":
                    serviceFactors.setFtpDiscovered(service.getValue());
                    break;
                case "telnet":
                    serviceFactors.setTelnetDiscovered(service.getValue());
                    break;
                case "smb":
                    serviceFactors.setSmbDiscovered(service.getValue());
                    break;
                case "imap":
                    serviceFactors.setImapDiscovered(service.getValue());
                    break;
                case "pop3":
                    serviceFactors.setPop3Discovered(service.getValue());
                    break;
                default:
                    break;
            }
        }
        ArrayList<Port> ports = new ArrayList<>();
        serviceFactors.setPorts(ports);
        return serviceFactors;
    }

    private List<Port> parsePorts(String jsonString) {
        log.info("Parsing Ports");
        JSONObject obj = new JSONObject(jsonString);
        List<Port> ports = new ArrayList<>();
        try {
            JSONArray services = obj.getJSONObject("nmaprun")
                    .getJSONObject("host")
                    .getJSONObject("ports")
                    .getJSONArray("port");

            for (int i = 0; i < services.length(); i++) {
                String service = services.getJSONObject(i).getJSONObject("service").getString("name");
                String state = services.getJSONObject(i).getJSONObject("state").getString("state");
                String portId = Integer.toString(services.getJSONObject(i).getInt("portid"));
                Port port = new Port(
                        service,
                        portId,
                        state
                );
                ports.add(port);
            }
            return ports;
        } catch (Exception e) {
            log.error("Exception in Kafka NetworkDiscovery->parsePorts Method :{} ", e.getMessage());
        }
        return null;
    }

    private Map<String, Boolean> parseDNS(String jsonString) {
        log.info("Parsing DNS");
        Map<String, Boolean> serviceDict = new HashMap<>();
        serviceDict.put("ftp", false);
        serviceDict.put("telnet", false);
        serviceDict.put("smb", false);
        serviceDict.put("imap", false);
        serviceDict.put("pop3", false);
        JSONObject obj = new JSONObject(jsonString);
        try {
            JSONArray services = obj.getJSONObject("nmaprun")
                    .getJSONObject("host")
                    .getJSONObject("ports")
                    .getJSONArray("port");

            for (int i = 0; i < services.length(); i++) {
                String service = services.getJSONObject(i).getJSONObject("service").getString("name");
                String state = services.getJSONObject(i).getJSONObject("state").getString("state");
                if (service.equals("ftp") || service.equals("telnet") || service.equals("smb") || service.equals("imap") || service.equals("pop3") && state.equals("open")) {
                    serviceDict.replace(service, true);
                }
            }
            return serviceDict;
        } catch (Exception e) {
            log.error("Exception in Kafka NetworkDiscovery->parseDNS Method :{} ", e.getMessage());
            return null;
        }

    }


    private Boolean parseNmapCmd(String jsonString) {
        log.info("Parsing Nmap Command");
        JSONObject obj = new JSONObject(jsonString);
        try {
            String state = obj.getJSONObject("nmaprun")
                    .getJSONObject("host")
                    .getJSONObject("ports")
                    .getJSONObject("port")
                    .getJSONObject("state")
                    .getString("state");
            if (state.equals("filtered") || state.equals("closed")) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            log.error("Exception in Kafka NetworkDiscovery->parseNmapCmd Method :{} ", e.getMessage());
            return false;
        }
    }

    private Boolean parseScript(String jsonString) {
        log.info("Parsing Script");
        JSONObject obj = new JSONObject(jsonString);
        try {
            Boolean hasInfo = obj.getJSONObject("nmaprun").getJSONObject("host").has("hostscript");
            return hasInfo.equals(Boolean.TRUE);
        } catch (Exception e) {
            log.error("Exception in Kafka NetworkDiscovery->parseScript Method :{} ", e.getMessage());
            return false;
        }
    }

    public SSLFactors discoverSSL(String target) {
        log.info("Discovering SSL.");
        String cmdTemplate = Constants.SSL_CMD;
        String fileName = this.makeFile("ssl", "json");
        RunProcess process = new RunProcess();
        String sslCmd = String.format(cmdTemplate, target, fileName);
        log.info(sslCmd);

        Boolean isFinished = process.runProcess(sslCmd);
        if (Boolean.TRUE.equals(isFinished)) {
            return this.parseSSL(fileName, target);
        }
        return null;
    }

    private SSLFactors parseSSL(String fileName, String target) {
        log.info("Parsing SSL");
        Parser parser = new Parser();
        JSONObject data = parser.getJson(fileName);
        parser.deleteFile(fileName);
        if (data != null && data.getJSONObject(target).isNull(target)) {
            data = data.getJSONObject(target);
            SSLFactors sslFactors = new SSLFactors();
            sslFactors.setIsExpired(data.getBoolean("cert_exp"));
            sslFactors.setGrade(data.isNull("grade") ? "" : data.getString("grade"));

            String issuedTo = data.isNull("issued_o") ? "" : data.getString("issued_o");
            String issuedBy = data.isNull("issuer_o") ? "" : data.getString("issuer_o");
            String issuedCountry = data.isNull("issuer_c") ? "" : data.getString("issuer_c");
            String validFrom = data.isNull("valid_from") ? "" : data.getString("valid_from");
            String validTill = data.isNull("valid_till") ? "" : data.getString("valid_till");


            sslFactors.setIssuedBy(issuedBy);
            sslFactors.setIssuedTo(issuedTo);
            sslFactors.setIssuedCountry(issuedCountry);
            sslFactors.setValidFrom(validFrom);
            sslFactors.setValidTill(validTill);

            if (issuedBy.equals(issuedTo)) {
                sslFactors.setIsSelfSigned(true);
            }
            String cipher = data.getString("cert_alg");

            sslFactors.setCipher(cipher);
            if (!cipher.contains("sha2") || !cipher.contains("sha256")) {
                sslFactors.setIsWeakCipher(true);
            }
            if (data.has("poodle_vuln")) {
                addVul("poodle", data.getBoolean("poodle_vuln"), sslFactors);
            }
            if (data.has("heartbeat_vuln")) {
                addVul("heartbeat", data.getBoolean("heartbeat_vuln"), sslFactors);
            }
            if (data.has("logjam_vuln")) {
                addVul("logjam", data.getBoolean("logjam_vuln"), sslFactors);
            }
            if (data.has("freak_vuln")) {
                addVul("freak", data.getBoolean("freak_vuln"), sslFactors);
            }
            if (data.has("heartbleed_vuln")) {
                addVul("heartbleed", data.getBoolean("heartbleed_vuln"), sslFactors);
            }
            if (data.has("drownVulnerable")) {
                addVul("drownVulnerable", data.getBoolean("drownVulnerable"), sslFactors);
            }

            return sslFactors;
        }
        return null;
    }

    public void addVul(String name, boolean isDiscovered, SSLFactors sslFactors) {
        Vulnerability vul = new Vulnerability(name, isDiscovered);
        sslFactors.getVulnerabilities().add(vul);
    }

    public SSHFactors discoverSSH(String target) {
        log.info("Discovering SSH");
        String cmdTemplate = Constants.SSH_CMD;
        String fileName = this.makeFile("ssh", "json");
        RunProcess process = new RunProcess();
        String sslCmd = String.format(cmdTemplate, target, fileName);
        log.info(sslCmd);
        Boolean isFinished = process.runProcess(sslCmd);
        if (Boolean.TRUE.equals(isFinished)) {
            return this.parseSSH(fileName);
        }
        return null;
    }

    private SSHFactors parseSSH(String fileName) {
        log.info("Parsing SSH");
        Parser parser = new Parser();
        JSONObject data = parser.getJson(fileName);
        if (data != null) {
            SSHFactors sshFactors = new SSHFactors();
            int version = data.getJSONObject("banner").getJSONArray("protocol").getInt(0);
            sshFactors.setSshVersion(version);
            for (int i = 0; i < data.getJSONArray("enc").length(); i++) {
                sshFactors.getCiphers().add(data.getJSONArray("enc").getString(i));
            }
            for (int i = 0; i < data.getJSONArray("mac").length(); i++) {
                sshFactors.getMac().add(data.getJSONArray("mac").getString(i));
            }

            parser.deleteFile(fileName);
            return sshFactors;
        }
        return null;
    }

    private String makeFile(String serviceName, String extension) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HHmmss-yyyyMMdd");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        log.info("Creating file : " + "scan-" + serviceName + time + "." + extension);
        return "scan-" + serviceName + time + "." + extension;
    }

}
