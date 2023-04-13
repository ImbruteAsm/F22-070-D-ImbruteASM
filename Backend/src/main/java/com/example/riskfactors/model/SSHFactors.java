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
public class SSHFactors {

    @Id
    @SequenceGenerator(
            name = "SSHFactorsSequence",
            sequenceName = "SSHFactorsSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SSHFactorsSequence"
    )
    private Long SSHFactorsId;
    private int sshVersion;
    @ElementCollection
    private List<String> ciphers;
    @ElementCollection
    private List<String> mac;

    public SSHFactors() {
        this.sshVersion = 0;
        this.ciphers = new ArrayList<>();
        this.mac = new ArrayList<>();
    }

}
