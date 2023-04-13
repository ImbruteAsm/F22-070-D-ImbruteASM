package com.example.riskfactors.model;

import lombok.*;
import org.hibernate.annotations.Table;
import javax.persistence.*;


@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationSecurity {

    @Id
    @SequenceGenerator(
            name = "ApplicationSecuritySequence",
            sequenceName = "ApplicationSecuritySequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ApplicationSecuritySequence"
    )
    private Long applicationSecurityId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "cookiesFK"
    )
    private Cookies cookies;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "generalId",
            referencedColumnName = "generalId"
    )
    private General general;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "highSeverityVulId",
            referencedColumnName = "highSeverityVulId"
    )
    private HighSeverityVul highSeverityVul;
    private float riskNumber;


    public ApplicationSecurity(Cookies cookies, General general, HighSeverityVul highSeverityVul) {
        this.cookies = cookies;
        this.general = general;
        this.highSeverityVul = highSeverityVul;
        this.riskNumber = 0;
    }
}
