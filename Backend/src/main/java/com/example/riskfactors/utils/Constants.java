package com.example.riskfactors.utils;

import org.apache.kafka.common.protocol.types.Field;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Constants {
    public static final Float CRITICAL = 1f;
    public static final Float HIGH = 0.8f;
    public static final Float MEDIUM = 0.6f;
    public static final Float LOW = 0.4f;
    public static final Float VLOW = 0.2f;


    public static Map<String, String> SERVICES_CMD = Stream.of(new String[][]{
            {"mongo", "nmap -p 27017 --script=mongodb-info -oX "},
            {"cassandra", "nmap -p 9160 --script=cassandra-info -oX "},
            {"microsoftSQL", "nmap -p 1433,4022,135,1434 --script=ms-sql-info -oX "},
            {"mySQL", "nmap --script=mysql-info -oX "},
            {"redis", "nmap --script=redis-info -oX "},
            {"dns", "nmap --script=dns-service-discovery -oX "},
            {"rsync", "nmap -p 873 -sV -oX "},
            {"rdp", "nmap -p 3389 -sV -oX "},
            {"vnc", "nmap --script=vnc-info -oX "},
    }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

    public static final String DETECTEDDEVICES_CMD = "nmap -sn %s -oX %s > /dev/null";

    public static final String OPERATINGSYSTEM_CMD = "sudo nmap -O %s -oX %s > /dev/null";


    public static final String SERVICE_CMD = "nmap -sV %s -oX %s > /dev/null";

    public static final String SSL_CMD = "python3 ./ssl-checker/ssl_checker.py -H  %s -a -j > %s";
    public static final String SSH_CMD = "ssh-audit %s -j > %s";
    public static final String SUBDOMAINS_CMD = "nmap --script dns-brute.nse %s -oX %s >/dev/null";
    public static final String ALIEN_VAULT_REQUEST = "https://otx.alienvault.com/api/v1/indicators/domain/%s/malware";
}
