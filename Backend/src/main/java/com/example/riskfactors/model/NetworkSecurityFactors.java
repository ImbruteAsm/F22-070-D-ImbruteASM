package com.example.riskfactors.model;

import lombok.*;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@Builder
public class NetworkSecurityFactors {

    @Id
    @SequenceGenerator(
            name = "networkSecurityFactorsSequence",
            sequenceName = "networkSecurityFactorsSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "networkSecurityFactorsSequence"
    )
    private Long networkSecurityFactorsId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "serviceFactorsFK"
    )
    private ServiceFactors serviceFactors;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "SSLFactorsFK"
    )
    private SSLFactors sslFactors;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "SSHFactorsFK"
    )
    private SSHFactors sshFactors;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "networkSecurityFactorsFK"
    )
    private List<Subdomain> subdomains;
    private float riskFactor;


    public NetworkSecurityFactors() {
        this.serviceFactors = new ServiceFactors();
        this.sslFactors = new SSLFactors();
        this.sshFactors = new SSHFactors();
        this.subdomains = new ArrayList<>();
        this.riskFactor = 0;
    }

}
