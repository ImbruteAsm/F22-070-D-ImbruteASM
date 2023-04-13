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
@NoArgsConstructor
@Builder
public class Subdomain {

    @Id
    @SequenceGenerator(
            name = "subdomainSequence",
            sequenceName = "subdomainSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subdomainSequence"
    )
    private Long subdomainId;
    private String url;
    private String ip;

}
