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
public class OperatingSystem {

    @Id
    @SequenceGenerator(
            name = "operatingSystemSequence",
            sequenceName = "operatingSystemSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "operatingSystemSequence"
    )
    private Long operatingSystemId;
    private String OSvendor;
    private String CPE;
    private String Type;
    private String OSFamily;
    private String OSname;

}
