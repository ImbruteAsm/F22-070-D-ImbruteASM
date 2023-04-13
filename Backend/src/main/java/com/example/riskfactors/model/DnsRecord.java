package com.example.riskfactors.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DnsRecord {
    private String name;
    private String ttl;
    private String dnsClass;
    private String type;
    private String ipAddress;

}
