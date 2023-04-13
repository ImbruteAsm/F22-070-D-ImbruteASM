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
public class SPF {

    @Id
    @SequenceGenerator(
            name = "SPFSequence",
            sequenceName = "SPFSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SPFSequence"
    )
    private Long SPFId;
    private boolean isSpfRecordMissing = true;
    private boolean isSpfRecordMalformed = true;
    private boolean doesSpfRecordContainsWildcard = false;
    private boolean doesspfRecordContainsaSoftfail = false;
    private String target;
}
