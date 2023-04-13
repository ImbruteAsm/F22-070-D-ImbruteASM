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
public class SSLFactors {


    @Id
    @SequenceGenerator(
            name = "SSLFactorsSequence",
            sequenceName = "SSLFactorsSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SSLFactorsSequence"
    )
    private Long SSLFactorsId;
    private String grade;
    private Boolean isSelfSigned;
    private Boolean isExpired;
    private Boolean isWeakCipher;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "vulnerabilityFK"
    )
    private List<Vulnerability> vulnerabilities;
    private String issuedTo;
    private String issuedBy;
    private String cipher;
    private String issuedCountry;
    private String validFrom;
    private String validTill;

    public SSLFactors() {
        this.grade = "";
        this.isSelfSigned = false;
        this.isExpired = false;
        this.isWeakCipher = false;
        this.vulnerabilities = new ArrayList<>();
        this.issuedBy = "";
        this.issuedTo = "";
        this.cipher = "";
        this.issuedCountry = "";
        this.validFrom = "";
        this.validTill = "";
    }


}
