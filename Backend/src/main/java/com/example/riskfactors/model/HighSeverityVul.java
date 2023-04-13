package com.example.riskfactors.model;

import lombok.*;
import javax.persistence.*;

@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HighSeverityVul {

    @Id
    @SequenceGenerator(
            name = "highSeverityVulSequence",
            sequenceName = "highSeverityVulSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "highSeverityVulSequence"
    )
    private Long highSeverityVulId;
    private Boolean xss;
    private Boolean csrf;
    private Boolean sqlI;

    public void setDefaults() {
        xss = Boolean.FALSE;
        csrf = Boolean.FALSE;
        sqlI = Boolean.FALSE;
    }
}
