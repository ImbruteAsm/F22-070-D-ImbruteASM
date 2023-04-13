package com.example.riskfactors.model;

import lombok.*;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

//dua
@Setter
@Getter
@Table()
@Entity()
@Data
@AllArgsConstructor()
@NoArgsConstructor
public class RiskFactors {


    @Id
    @SequenceGenerator(
            name = "riskFactorsSequence",
            sequenceName = "riskFactorsSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "riskFactorsSequence"
    )
    private Long RiskFactorsId;
    private String target;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "networkSecurityFactorsFK"
    )
    private NetworkSecurityFactors networkSecurityFactors;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "endpointSecurityFK"
    )
    private EndpointSecurity endpointSecurity;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "dnsHealthFK"
    )
    private DNSHealth dnsHealth;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "applicationSecurityFactorsFK"
    )
    private ApplicationSecurity applicationSecurityFactors;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "ipReputationFK"
    )
    private IpReputation ipReputation;
    private float riskNumber;

    public RiskFactors(NetworkSecurityFactors networkSecurityFactors, DNSHealth dnsHealth, ApplicationSecurity applicationSecurityFactors, IpReputation ipRepuation, EndpointSecurity endpointSecurity) {

        this.networkSecurityFactors = networkSecurityFactors;
        this.dnsHealth = dnsHealth;
        this.applicationSecurityFactors = applicationSecurityFactors;
        this.ipReputation = ipRepuation;
        this.endpointSecurity = endpointSecurity;
        this.riskNumber = 0f;
    }

}
