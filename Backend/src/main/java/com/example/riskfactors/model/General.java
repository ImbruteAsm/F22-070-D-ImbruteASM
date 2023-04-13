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
public class General {

    @Id
    @SequenceGenerator(
            name = "generalSequence",
            sequenceName = "generalSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "generalSequence"
    )
    private Long generalId;
    private Boolean enforceHttps;
    private Boolean CORSPolicy;
    private Boolean csp;
    private Boolean hsts;
    private Boolean xFrameOptions;
    private Boolean xContentTypeOptions;
    private Boolean unencryptedPwd;

    public void setDefaults() {
        enforceHttps = Boolean.FALSE;
        CORSPolicy = Boolean.FALSE;
        csp = Boolean.FALSE;
        hsts = Boolean.FALSE;
        xFrameOptions = Boolean.FALSE;
        xContentTypeOptions = Boolean.FALSE;
        unencryptedPwd = Boolean.FALSE;
    }


}
