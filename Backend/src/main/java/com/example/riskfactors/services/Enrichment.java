package com.example.riskfactors.services;

import com.example.riskfactors.model.*;
import com.example.riskfactors.utils.Constants;
import com.example.riskfactors.utils.RiskFactorsRiskNumberCalculation;
import com.example.riskfactors.utils.RunProcess;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class Enrichment {
    @Autowired
    private RiskFactorsRiskNumberCalculation riskFactorsRiskNumberCalculation;
    @Autowired
    private NetworkDiscovery networkDiscovery;
    @Autowired
    private EndpointDetection endPointDetection;
    @Value("${X-OTX-API-KEY}")
    private String apiKey;
    @Value("${NMAP-BASE-PATH}")
    private String nmapBasePath;

    //the getFactors function receives the target host as a parameter and extracts various security factors to be used
    //to calculate the risk score. This particular function looks at network security and endpoint security
    public RiskFactors getFactors(String target) throws IOException, InterruptedException {

        NetworkSecurityFactors networkSecurityFactors = gatherDataNetworkSecurity(target);
        EndpointSecurity endpointSecurity = gatherEndpointSecurity(target);
        IpReputation ipReputation = gatherDataIpReputation(target);
        DNSHealth dnsHealth = gatherDataDNSHealth(target);
        ApplicationSecurity applicationSecurity = gatherDataApplicationSecurity(target);

        RiskFactors riskFactors = new RiskFactors();
        riskFactors.setNetworkSecurityFactors(networkSecurityFactors);
        riskFactors.setEndpointSecurity(endpointSecurity);
        riskFactors.setDnsHealth(dnsHealth);
        riskFactors.setApplicationSecurityFactors(applicationSecurity);
        riskFactors.setIpReputation(ipReputation);

        return riskFactors;
    }

    //this function is used to gather all endpoint factors for example: devices, services, operating system
    private EndpointSecurity gatherEndpointSecurity(String target){
        try {
            Devices devices = this.endPointDetection.discoveredDevices(target);
            List<Services> services = this.endPointDetection.detectServices(target);
            List<OperatingSystem> operatingSystems = this.endPointDetection.detectOperatingSystem(target);

            EndpointSecurity endpointSecurity = new EndpointSecurity(operatingSystems,devices,services);
            return endpointSecurity;
        }
        catch(Exception e){
            log.error("Not working",e);
        }
        return null;
    }

    //this function is used to gather all network security factors for example: services, SSL, SSH, subdomain
    private NetworkSecurityFactors gatherDataNetworkSecurity(String target) {
        log.info("Gathering Network Security Factors");
        NetworkSecurityFactors networkSecurityFactors = new NetworkSecurityFactors();
        try {
            ServiceFactors serviceFactors = this.networkDiscovery.discoverServices(target);
            SSLFactors sslFactors = this.networkDiscovery.discoverSSL(target);
            SSHFactors sshFactors = this.networkDiscovery.discoverSSH(target);
            List<Subdomain> subdomains = this.networkDiscovery.discoverSubDomains(target);
            networkSecurityFactors.setServiceFactors(serviceFactors);
            networkSecurityFactors.setSslFactors(sslFactors);
            networkSecurityFactors.setSshFactors(sshFactors);
            networkSecurityFactors.setSubdomains(subdomains);
            determineRiskNumber(networkSecurityFactors);
            return networkSecurityFactors;

        } catch (IOException e) {
            log.error("Exception in Kafka Enrichment->gatherDataNetworkSecurity Method :{} ", e.getMessage());
        }
        return null;
    }


    private float getServiceScore(NetworkSecurityFactors networkSecurityFactors) {
        log.info("Generating Service Score");
        float score = 0;
        if (networkSecurityFactors.getServiceFactors() != null) {
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getMongoDiscovered())) {
                score += Constants.HIGH;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getCassandraDiscovered())) {
                score += Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getMsSQLDiscovered())) {
                score += Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getMySQLDiscovered())) {
                score += Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getRedisDiscovered())) {
                score += Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getRdpDiscovered())) {
                score += Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getRsyncDiscovered())) {
                score += Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getVncDiscovered())) {
                score += Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getFtpDiscovered())) {
                score += Constants.LOW;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getSmbDiscovered())) {
                score += Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getServiceFactors().getTelnetDiscovered())) {
                score += Constants.LOW;
            }
        }
        return score;
    }

    public float getSslScore(NetworkSecurityFactors networkSecurityFactors) {
        log.info("Generating SSL Score");
        float score = 0f;
        if (networkSecurityFactors.getSslFactors() != null) {
            if (Boolean.TRUE.equals(networkSecurityFactors.getSslFactors().getIsExpired())) {
                score += Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getSslFactors().getIsWeakCipher())) {
                score += 2 * Constants.MEDIUM;
            }
            if (Boolean.TRUE.equals(networkSecurityFactors.getSslFactors().getIsSelfSigned())) {
                score += Constants.MEDIUM;
            }
            for (Vulnerability vul : networkSecurityFactors.getSslFactors().getVulnerabilities()) {
                if (Boolean.TRUE.equals(vul.getIsDiscovered())) {
                    score += Constants.HIGH;
                }
            }
        }
        return score;
    }

    private float getSshScore(NetworkSecurityFactors networkSecurityFactors) {
        log.info("Generating SSH Score");
        float score = 0;
        if (networkSecurityFactors.getSshFactors() != null) {
            if (networkSecurityFactors.getSshFactors().getSshVersion() < 2) {
                score += Constants.HIGH;
            }
            for (String cipher : networkSecurityFactors.getSshFactors().getCiphers()) {
                if (cipher.contains("cbc") || cipher.contains("rc4")) {
                    score += Constants.MEDIUM;
                }
            }
            for (String cipher : networkSecurityFactors.getSshFactors().getMac()) {
                if (cipher.contains("md5")) {
                    score += Constants.MEDIUM;
                }
            }
            return score;
        }
        return 0;
    }

    public void determineRiskNumber(NetworkSecurityFactors networkSecurityFactors) {
        log.info("Generating Risk Factor");
        float score;
        float serviceNumber = this.getServiceScore(networkSecurityFactors);
        float sslNumber = this.getSslScore(networkSecurityFactors);
        float sshNumber = this.getSshScore(networkSecurityFactors);
        log.info(serviceNumber + " " + sshNumber + " " + sslNumber);
        score = Constants.MEDIUM * (sslNumber + serviceNumber + sshNumber);
        networkSecurityFactors.setRiskFactor(score);

    }


    public DNSHealth gatherDataDNSHealth(String target) {
        DNSHealth dnsHealth = new DNSHealth();
        try {
            dnsHealth.getSpf().setTarget(target);
            checkSpfRecordandValidateIt(target, dnsHealth);
            getServers(target, dnsHealth);
            checkOpenDNS(target, dnsHealth);
            getDNSHealthRiskScore(dnsHealth);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->gatherDataDNSHealth Method :{} ", e.getMessage());
        }
        return dnsHealth;
    }

    public void checkSpfRecordandValidateIt(String target, DNSHealth dnsHealth) {

        log.info("Running SPF Check");
        StringBuilder output = new StringBuilder();

        Process p;
        String returnValue = null;
        try {
            p = Runtime.getRuntime().exec("dig -t TXT " + target + " +short");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            returnValue = "";
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains("spf")) {
                    if (line.contains("~")) {
                        dnsHealth.getSpf().setDoesspfRecordContainsaSoftfail(true);

                    }
                    if (line.contains("*")) {

                        dnsHealth.getSpf().setDoesSpfRecordContainsWildcard(true);
                    }
                    returnValue = line;
                    log.info(line);
                }
                output.append(line + "\n");
            }

        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->checkSpfRecordandValidateIt Method :{} ", e.getMessage());
        }

        if (returnValue == null) {

            dnsHealth.getSpf().setSpfRecordMissing(true);
            dnsHealth.getSpf().setSpfRecordMalformed(true);
        } else {

            dnsHealth.getSpf().setSpfRecordMissing(false);
            dnsHealth.getSpf().setSpfRecordMalformed(false);
        }
    }

    private void getServers(String target, DNSHealth dnsHealth) {
        log.info("Generating List Of ??Servers");
        String cmdTemplate = "dig +nocmd %s +noall +answer | awk '{if (NR>3){print}}'| tr -s '\\t' | jq -R 'split(\"\\t\") |{Name:.[0],TTL:.[1],Class:.[2],Type:.[3],IpAddress:.[4]}' | jq --slurp . > %s";
        String fileName = target + ".json";
        RunProcess process = new RunProcess();
        String subdomainCmd = String.format(cmdTemplate, target, fileName);
        Boolean isFinished = process.runProcess(subdomainCmd);
        if (isFinished) {
            try {
                JSONArray data = new JSONArray(new JSONTokener(new FileReader(fileName)));
                for (int i = 0; i < data.length(); i++) {
                    JSONObject obj = data.getJSONObject(i);
                    System.out.println(obj);
                    String type = obj.getString("Type").toLowerCase();
                    String value = obj.getString("IpAddress");
                    if (type.equalsIgnoreCase("mx")) {
                        dnsHealth.getMx().add(value);
                    } else if (type.equalsIgnoreCase("ns")) {
                        dnsHealth.getNs().add(value);
                    } else if (type.equalsIgnoreCase("txt")) {
                        dnsHealth.getTxt().add(value);
                    } else if (type.equalsIgnoreCase("soa")) {
                        dnsHealth.getSoa().add(value);
                    } else if (type.equalsIgnoreCase("a")) {
                        dnsHealth.getA().add(value);
                    } else if (type.equalsIgnoreCase("cname")) {
                        dnsHealth.getCName().add(value);
                    } else if (type.equalsIgnoreCase("aname")) {
                        dnsHealth.getAName().add(value);
                    } else if (type.equalsIgnoreCase("srv")) {
                        dnsHealth.getSrv().add(value);
                    } else if (type.equalsIgnoreCase("ptr")) {
                        dnsHealth.getPtr().add(value);
                    } else if (type.equalsIgnoreCase("aaaa")) {
                        dnsHealth.getQuadA().add(value);
                    } else {
                        log.info("UNKNOWN VALUE " + value);
                    }
                }
            } catch (Exception e) {
                log.error("Exception in Kafka Enrichment->getServers Method :{} ", e.getMessage());
            }
        }
    }

    void checkOpenDNS(String target, DNSHealth dnsHealth) {
        log.info("Running Open DNS Resolver");

        StringBuilder output = new StringBuilder();

        Process p;
        try {
            p = Runtime.getRuntime().exec("dig +short test.openresolver.com TXT " + target);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                log.info(line);

                if (line.contains("open-resolver-detected")) {
                    dnsHealth.setOpenDNSDetected(true);

                }
                output.append(line + "\n");
            }
            if (dnsHealth.isOpenDNSDetected()) {
                log.info("open-resolver-detected");
            } else {
                log.info("open-resolver-not-detected");
            }

        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->checkOpenDNS Method :{} ", e.getMessage());
        }
    }

    public void getDNSHealthRiskScore(DNSHealth dnsHealth) {
        log.info("Generating DNS Health Score");
        try {
            float riskScore = (dnsHealth.isOpenDNSDetected() ? Constants.HIGH * (Constants.CRITICAL + getSpfScore(dnsHealth.getSpf())) : Constants.HIGH * (getSpfScore(dnsHealth.getSpf())));
            dnsHealth.setRiskScore(riskScore);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->getDNSHealthRiskScore Method :{} ", e.getMessage());
        }

    }

    public int getSpfScore(SPF spf) {
        log.info("Generating SPF Score");
        int score = 0;
        try {
            if (spf.isSpfRecordMissing()) {
                score += 5;
            }
            if (spf.isSpfRecordMalformed()) {
                score += 1;
            }
            if (spf.isDoesSpfRecordContainsWildcard()) {
                score += 1;
            }
            if (spf.isDoesspfRecordContainsaSoftfail()) {
                score += 1;
            }
        } catch (Exception e) {
            log.error("Error : ", e.getMessage());
        }
        return score;
    }

    public IpReputation gatherDataIpReputation(String target) {
        log.info("Gathering IP Reputation");
        try {
            String urlStr = String.format(Constants.ALIEN_VAULT_REQUEST, target);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-OTX-API-KEY", this.apiKey);
            StringBuilder content;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            IpReputation ipReputation = this.parseAlienVault(content.toString());
            if (ipReputation != null) {
                ipReputation = determineRiskNumber(ipReputation);
                return ipReputation;
            }
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->gatherDataIpReputation Method :{} ", e.getMessage());
        }
        return null;
    }

    private IpReputation parseAlienVault(String jsonString) {
        try {
            JSONObject data = new JSONObject(jsonString);
            log.info(data.toString(2));
            if (data.getInt("size") > 0) {
                IpReputation ipReputation = new IpReputation();
                JSONArray malwares = data.getJSONArray("data");
                for (int i = 0; i < malwares.length(); i++) {
                    JSONObject malware = malwares.getJSONObject(i).getJSONObject("detections");
                    if (!malware.isNull("avast")) {
                        ipReputation = addMalware(malware.getString("avast"), ipReputation);
                    }
                    if (!malware.isNull("avg")) {
                        ipReputation = addMalware(malware.getString("avg"), ipReputation);
                    }
                    if (!malware.isNull("clamav")) {
                        ipReputation = addMalware(malware.getString("clamav"), ipReputation);
                    }
                    if (!malware.isNull("msdefender")) {
                        ipReputation = addMalware(malware.getString("msdefender"), ipReputation);
                    }
                }
                return ipReputation;
            }
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->parseAlienVault Method :{} ", e.getMessage());
        }
        return null;
    }

    public IpReputation addMalware(String malware, IpReputation ipReputation) {
        try {
            if (!ipReputation.getMalwareInfection().getMalwares().contains(malware))
                ipReputation.getMalwareInfection().getMalwares().add(malware);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->addMalware Method :{} ", e.getMessage());
        }
        return ipReputation;
    }


    public IpReputation determineRiskNumber(IpReputation ipReputation) {
        try {
            ipReputation.setRiskFactor(ipReputation.getMalwareInfection().getMalwares().isEmpty() ? Constants.HIGH : 0f);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->determineRiskNumber Method :{} ", e.getMessage());
        }
        return ipReputation;
    }

    public int toInt(Boolean x) {
        return x ? 1 : 0;
    }

    public ApplicationSecurity gatherDataApplicationSecurity(String target) throws IOException, InterruptedException {
        // currently, hardcoded the output of the nmap scanner
        // currently, hardcoded the output of the arachni scanner
        ApplicationSecurity appSecurity = null;
        List<String> issuesN = null;
        ArrayList<String> issues = new ArrayList<>(Arrays.asList("Cross-Site Scripting (XSS) in script context", "hsts missing", "insecure cors", "sql vul"));
        try {

            // running nmap scanner
            EnrichmentTools instance2 = new NMap();
            ((NMap) instance2).setPath(nmapBasePath);

            issuesN = ((NMap) instance2).scanApplication(target);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->gatherDataApplicationSecurity Method in NMAP Usage :{} ", e.getMessage());
            e.printStackTrace();
        }
        log.info("*****************FINAL ISSUES************");
        try {
            for (String issue : issues) log.info(issue);
            for (String issue : issuesN) log.info(issue);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->gatherDataApplicationSecurity Method while printing issues :{} ", e.getMessage());
        }
        // going over both issues list and assigning which vulnerailities/risks found or not by true false
        General genSecurity = new General();
        HighSeverityVul highVul = new HighSeverityVul();
        Cookies cookiesVul = new Cookies();
        genSecurity.setDefaults();
        highVul.setDefaults();
        cookiesVul.setDefaults();
        try {

            if (issuesN.contains("X_Frame_Options")) genSecurity.setXFrameOptions(Boolean.TRUE);
            if (issuesN.contains("X_Content_Type_Options")) genSecurity.setXContentTypeOptions(Boolean.TRUE);
            if (issuesN.contains("Content-Security-Policy")) genSecurity.setCsp(Boolean.TRUE);
            if (issues.stream().anyMatch(s -> s.contains("hsts"))) genSecurity.setHsts(Boolean.TRUE);
            if (issues.stream().anyMatch(s -> s.contains("CORS")) || issues.stream().anyMatch(s -> s.contains("cors")))
                genSecurity.setCORSPolicy(Boolean.TRUE);
            if (issues.stream().anyMatch(s -> s.contains("http"))) genSecurity.setCORSPolicy(Boolean.TRUE);
            if (issues.stream().anyMatch(s -> s.contains("Unencrypted password form")))
                genSecurity.setUnencryptedPwd(Boolean.TRUE);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->gatherDataApplicationSecurity Method while setting genSecurity issue type :{} ", e.getMessage());
        }
        try {

            if (issuesN.contains("csrf")) highVul.setCsrf(Boolean.TRUE);
            if (issues.stream().anyMatch(s -> s.contains("XSS"))) highVul.setXss(Boolean.TRUE);
            if (issues.stream().anyMatch(s -> s.contains("SQL")) || issues.stream().anyMatch(s -> s.contains("sql")))
                highVul.setSqlI(Boolean.TRUE);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->gatherDataApplicationSecurity Method while setting highVul issue type :{} ", e.getMessage());
        }
        try {

            if (issuesN.contains("secure")) cookiesVul.setSecure(Boolean.TRUE);
            if (issuesN.contains("httponly")) cookiesVul.setHttpOnly(Boolean.TRUE);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->gatherDataApplicationSecurity Method while setting cookiesVul issue type :{} ", e.getMessage());
        }

        try {

            appSecurity = new ApplicationSecurity(cookiesVul, genSecurity, highVul);
            appSecurity = determineRiskNumber(appSecurity);
        } catch (Exception e) {
            log.error("Exception in Kafka Enrichment->gatherDataApplicationSecurity Method :{} ", e.getMessage());
            e.printStackTrace();
        }
        return appSecurity;
    }

    public ApplicationSecurity determineRiskNumber(ApplicationSecurity appSecurity) {
        log.info("Generating Application Security Score");
        float riskNumber = this.toInt(appSecurity.getHighSeverityVul().getCsrf()) * Constants.HIGH
                + this.toInt(appSecurity.getHighSeverityVul().getSqlI()) * Constants.HIGH
                + this.toInt(appSecurity.getHighSeverityVul().getXss()) * Constants.HIGH
                + this.toInt(appSecurity.getGeneral().getEnforceHttps()) * Constants.HIGH
                + this.toInt(appSecurity.getGeneral().getCsp()) * Constants.HIGH
                + this.toInt(appSecurity.getGeneral().getHsts()) * Constants.MEDIUM
                + this.toInt(appSecurity.getCookies().getSecure()) * Constants.LOW
                + this.toInt(appSecurity.getCookies().getHttpOnly()) * Constants.LOW
                + this.toInt(appSecurity.getGeneral().getCORSPolicy()) * Constants.LOW
                + this.toInt(appSecurity.getGeneral().getXContentTypeOptions()) * Constants.LOW
                + this.toInt(appSecurity.getGeneral().getXFrameOptions()) * Constants.LOW;

        appSecurity.setRiskNumber(riskNumber);

        return appSecurity;

    }
}
