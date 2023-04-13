package com.example.riskfactors.services;

import com.example.riskfactors.model.*;
import com.example.riskfactors.utils.Constants;
import com.example.riskfactors.utils.Parser;
import com.example.riskfactors.utils.RunProcess;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class EndpointDetection {

    public Devices detectEndpoint(String target){
       // String os = detectOperatingSystem(target);
      //  String service = detectServices(target);
        Devices devices = discoveredDevices(target);
       // String results = os + service + devices;
        //return results;
        return devices;
    }

    public List<OperatingSystem> detectOperatingSystem(String target){
        log.info("Detecting Operating System");
        String cmdTemplate = Constants.OPERATINGSYSTEM_CMD;
        String fileName = this.makeFile("operatingsystem", "xml");
        RunProcess process = new RunProcess();
        String operatingsystemCmd = String.format(cmdTemplate, target, fileName);
        Boolean isFinished = process.runProcess(operatingsystemCmd);
        if (isFinished.equals(Boolean.TRUE)) {
            Parser converter = new Parser();
            try {
                String jsonString = converter.convert(fileName);
                //return jsonString;
                return parseOperatingSystem(jsonString);
                //return jsonString;
            } catch (Exception e) {
                log.error("Exception in Kafka NetworkDiscovery->discoverSubDomains Method :{} ", e.getMessage());
            }
        }
        return null;
    }

    public List<OperatingSystem> parseOperatingSystem(String jsonString){
        log.info("Parsing Operating System");
        JSONObject data = new JSONObject(jsonString);
        boolean isHostThere = false;
        List<OperatingSystem> operatingSystemList = new ArrayList<>();
        JSONObject os = null;
        try {
            isHostThere = data.getJSONObject("nmaprun").has("host");
            if (isHostThere) {
                JSONObject hostData = data.getJSONObject("nmaprun").getJSONObject("host");

                boolean isOSThere = false;
                isOSThere = hostData.has("os");


                if (isOSThere) {

                    os = hostData.getJSONObject("os");
                    boolean isThereOsmatch = os.has("osmatch");

                    if(isThereOsmatch){
                        System.out.println(">>>> " + os);
                        if (os.get("osmatch") instanceof JSONArray) {
                            JSONArray OS = os.getJSONArray("osmatch");

                            for(int i=0; i <OS.length(); i++){

                                System.out.println(i);

                                JSONObject element = OS.getJSONObject(i);
                                List<OperatingSystem> operatingSystems = extractOSList(element);
                                for (int j = 0; j < operatingSystems.size(); j++) {
                                    operatingSystemList.add(operatingSystems.get(j));
                                }

                            }
                        }
                        else {
                            JSONObject element = os.getJSONObject("osmatch");

                            List<OperatingSystem> operatingSystems = extractOSList(element);
                            for (int j = 0; j < operatingSystems.size(); j++) {
                                operatingSystemList.add(operatingSystems.get(j));
                            }
                        }
                    }
                }
                System.out.println(operatingSystemList.toString());
                return operatingSystemList;
            }
        }catch (Exception e){
            log.error("Error in Parsing Operating System ", e);
        }
        return null;
    }

    private List<OperatingSystem> extractOSList(JSONObject element) {

        List<OperatingSystem> osList = new ArrayList<>();

        if (element.has("osclass")) {
            if(element.get("osclass") instanceof JSONObject) {
                OperatingSystem operatingSystem = new OperatingSystem();

                JSONObject osElement = element.getJSONObject("osclass");
                setOSDetailsFromJSON(osList, operatingSystem, osElement);
            }
            else {
                JSONArray osClass = element.getJSONArray("osclass");
                for (int i = 0; i < osClass.length(); i++) {
                    OperatingSystem operatingSystem = new OperatingSystem();

                    JSONObject osElement = osClass.getJSONObject(i);
                    setOSDetailsFromJSON(osList, operatingSystem, osElement);

                }
            }
        }

        return osList;

    }

    private void setOSDetailsFromJSON(List<OperatingSystem> osList, OperatingSystem operatingSystem, JSONObject ops) {
        boolean isTherecpe;
        boolean isTherename;
        boolean isTHerevendor;
        boolean isTheretype;
        boolean isTherefamily;
        isTherecpe = ops.has("cpe");
        isTherename = ops.has("name");
        isTHerevendor = ops.has("vendor");
        isTheretype = ops.has("type");
        isTherefamily = ops.has("osfamily");

        if(isTherefamily) {
            operatingSystem.setOSFamily(ops.getString("osfamily"));
        }
        if(isTheretype) {
            operatingSystem.setType(ops.getString("type"));
        }
        if(isTherename) {
            operatingSystem.setOSname(ops.getString("name"));
        }
        if(isTherecpe) {
            operatingSystem.setCPE(ops.getString("cpe"));
        }
        if(isTHerevendor) {
            operatingSystem.setOSvendor(ops.getString("vendor"));
        }
        osList.add(operatingSystem);
    }

    public List<Services> detectServices(String target){
        log.info("Detecting Services");
        String cmdTemplate = Constants.SERVICE_CMD;
        String fileName = this.makeFile("detectservice", "xml");
        RunProcess process = new RunProcess();
        String detecctServiceCmd = String.format(cmdTemplate, target, fileName);
        Boolean isFinished = process.runProcess(detecctServiceCmd );
        if (isFinished.equals(Boolean.TRUE)) {
            Parser converter = new Parser();
            try {
                String jsonString = converter.convert(fileName);
                List<Services> testing = new ArrayList<>();
                testing = parseDetectedServices(jsonString);
                return parseDetectedServices(jsonString);
                //return jsonString;
            } catch (Exception e) {
                log.error("Failure in Parsing Services");
                //log.error("Exception in Kafka NetworkDiscovery->discoverSubDomains Method :{} ", e.getMessage());
            }
        }
        return null;

    }

    public List<Services> parseDetectedServices(String jsonString){
        log.info("Parsing Detected Services");
        JSONObject data = new JSONObject(jsonString);
        boolean isServiceThere = false;
        String fin=null;
        List<Services> serviceList = new ArrayList<>();
        try {
            isServiceThere = data.getJSONObject("nmaprun").getJSONObject("host").has("ports");
            if (isServiceThere) {
                JSONObject serviceData = data.getJSONObject("nmaprun").getJSONObject("host").getJSONObject("ports");
                boolean isTherePort = false;
                isTherePort = serviceData.has("port");
                if(isTherePort){
                    System.out.println("found it");

                    JSONArray portList = serviceData.getJSONArray("port");
                    for (int i = 0; i < portList.length(); i++) {
                        Services service= new Services();

                        JSONObject element = portList.getJSONObject(i);

                        boolean isThereProtocol = false;
                        isThereProtocol = element.has("protocl");
                        if(isThereProtocol){
                            service.setProtocol(element.getString("protocol"));
                        }

                        boolean isTherePortid = false;
                        isTherePortid = element.has("portid");
                        if(isTherePortid){
                            service.setPortID(element.getInt("portid"));
                        }


                        boolean isThereServ = false;
                        isThereServ = element.has("service");
                        if(isThereServ) {

                            JSONObject serviceObj = element.getJSONObject("service");
                            boolean isThereName = false, isThereConf = false, isThereMethod = false,
                                    isThereOSType = false, isThereProduct = false, isThereVersion = false;
                            isThereConf = serviceObj.has("conf");
                            isThereMethod = serviceObj.has("method");
                            isThereOSType = serviceObj.has("ostype");
                            isThereProduct = serviceObj.has("product");
                            isThereVersion = serviceObj.has("version");
                            isThereName = serviceObj.has("name");

                            if(isThereName) {
                                service.setName(serviceObj.getString("name"));
                            }
                            if(isThereConf) {
                                service.setConf(serviceObj.getInt("conf"));
                            }
                            if(isThereMethod) {
                                service.setMethod(serviceObj.getString("method"));
                            }
                            if(isThereOSType) {
                                service.setOStype(serviceObj.getString("ostype"));
                            }
                            if(isThereProduct) {
                                service.setProduct(serviceObj.getString("product"));
                            }
                            if(isThereVersion) {
                                service.setVersion(serviceObj.getInt("version"));
                            }

                        }
                        serviceList.add(service);
                    }

                }

                return serviceList;
            }
        }catch (Exception e){
            log.error("Error in ParseService ", e);
        }
        return null;
    }
    public Devices discoveredDevices(String target) {
        log.info("Discovering Devices");
        String cmdTemplate = Constants.DETECTEDDEVICES_CMD;
        String fileName = this.makeFile("detecteddevice", "xml");
        RunProcess process = new RunProcess();
        String detecteddeviceCmd = String.format(cmdTemplate, target, fileName);
        Boolean isFinished = process.runProcess(detecteddeviceCmd );
        if (isFinished.equals(Boolean.TRUE)) {
            Parser converter = new Parser();
            try {
                String jsonString = converter.convert(fileName);
                return parseDetectedDevices(jsonString);
                //return jsonString;
            } catch (Exception e) {
                //log.error("Exception in Kafka NetworkDiscovery->discoverSubDomains Method :{} ", e.getMessage());
                log.error("Failure while discovering devices");
            }
        }
        return null;
    }

    //from the JSON we extract the address, hostnames and status
    private Devices parseDetectedDevices(String jsonString){
        log.info("Parsing Detected Devices");
        JSONObject data = new JSONObject(jsonString);
        List<Hostname> hostnameList = new ArrayList<>();
        boolean isHostThere = false, isRunstatsThere = false;
        Devices devi = new Devices();

        JSONObject address= null, hostnames=null, status=null, runstats=null;
        try {
            isHostThere = data.getJSONObject("nmaprun").has("host");
            if (isHostThere){
                JSONObject hostData = data.getJSONObject("nmaprun").getJSONObject("host");
                boolean isAddressThere = false, isHostnamesThere= false, isStatusThere=false;
                isAddressThere = hostData.has("address");
                isHostnamesThere = hostData.has("hostnames");
                isStatusThere = hostData.has("status");
                if(isAddressThere){
                    address = hostData.getJSONObject("address");
                    String s = address.toString();
                    devi.setAddrType(address.getString("addrtype"));
                    devi.setAddr(address.getString("addr"));

                }
                if(isHostnamesThere){
                    if(hostData.getJSONObject("hostnames").get("hostname") instanceof JSONArray){
                        JSONArray hostname = hostData.getJSONObject("hostnames").getJSONArray("hostname");
                        for (int i = 0; i < hostname.length(); i++) {
                            Hostname host = new Hostname();
                            JSONObject element = hostname.getJSONObject(i);
                            host.setHostName(element.getString("name"));
                            host.setHostType(element.getString("type"));
                            hostnameList.add(host);
                        }
                        devi.setHosts(hostnameList);
                    }
                    else{
                        JSONObject hostname = hostData.getJSONObject("hostnames").getJSONObject("hostname");
                        Hostname host = new Hostname();
                        host.setHostName(hostname.getString("name"));
                        host.setHostType(hostname.getString("type"));
                        hostnameList.add(host);
                        devi.setHosts(hostnameList);
                    }
                }
            }
            isRunstatsThere = data.getJSONObject("nmaprun").has("runstats");
            if(isRunstatsThere){
                JSONObject runstatsData = data.getJSONObject("nmaprun").getJSONObject("runstats");
                boolean isRunstats = false;
                isRunstats = runstatsData.has("hosts");
                if(isRunstats){
                    runstats = runstatsData.getJSONObject("hosts");
                    devi.setTotal(runstats.getInt("total"));
                    devi.setUp(runstats.getInt("up"));
                    devi.setDown(runstats.getInt("down"));
                }
            }
            return devi;

        }catch (Exception e){
            log.error("Exception in EndpointDetection -> parseDetectedDevices` Method :{} ", e.getMessage());
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
