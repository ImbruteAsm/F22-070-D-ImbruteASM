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
public class Cookies {

    @Id
    @SequenceGenerator(
            name = "cookiesSequence",
            sequenceName = "cookiesSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "cookiesSequence"
    )
    private Long cookiesId;
    private Boolean httpOnly;
    private Boolean secure;

    public void setDefaults() {
        httpOnly = Boolean.FALSE;
        secure = Boolean.FALSE;
    }

}
