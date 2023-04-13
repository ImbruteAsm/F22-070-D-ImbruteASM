package com.example.riskfactors.model;

import lombok.*;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@Builder
public class IpReputation {

    @Id
    @SequenceGenerator(
            name = "ipReputationSequence",
            sequenceName = "ipReputationSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ipReputationSequence"
    )
    private Long ipReputationId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "malwareFK"
    )
    private Malware malwareInfection;
    private float riskFactor;

    public IpReputation() {
        this.malwareInfection = new Malware();
        this.riskFactor = 0f;
    }

}
