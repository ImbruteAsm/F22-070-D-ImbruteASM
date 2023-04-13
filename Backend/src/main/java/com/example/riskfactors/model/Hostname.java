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
public class Hostname {

    @Id
    @SequenceGenerator(
            name = "hostnameSequence",
            sequenceName = "hostnameSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "hostnameSequence"
    )
    private Long hostNameId;
    private String hostName;
    private String hostType;
}
