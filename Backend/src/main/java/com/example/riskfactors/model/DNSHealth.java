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
public class DNSHealth {

    @Id
    @SequenceGenerator(
            name = "DNSHealthSequence",
            sequenceName = "DNSHealthSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "DNSHealthSequence"
    )
    private Long dnsHealthId;
    private float riskScore;
    private boolean isOpenDNSDetected = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "spfFK"
    )
    private SPF spf;

    @ElementCollection
    private List<String> ns;
    @ElementCollection
    private List<String> mx;
    @ElementCollection
    private List<String> txt;
    @ElementCollection
    private List<String> a;
    @ElementCollection
    private List<String> quadA;
    @ElementCollection
    private List<String> aName;
    @ElementCollection
    private List<String> cName;
    @ElementCollection
    private List<String> soa;
    @ElementCollection
    private List<String> srv;
    @ElementCollection
    private List<String> ptr;

    public DNSHealth() {
        spf = new SPF();
        this.ns = new ArrayList<>();
        this.mx = new ArrayList<>();
        this.txt = new ArrayList<>();
        this.a = new ArrayList<>();
        this.quadA = new ArrayList<>();
        this.aName = new ArrayList<>();
        this.cName = new ArrayList<>();
        this.soa = new ArrayList<>();
        this.srv = new ArrayList<>();
        this.ptr = new ArrayList<>();

    }
}
