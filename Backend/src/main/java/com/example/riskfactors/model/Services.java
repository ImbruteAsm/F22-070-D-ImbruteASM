package com.example.riskfactors.model;

import lombok.*;
import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import java.util.List;
@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Services {

    @Id
    @SequenceGenerator(
            name = "servicesSequence",
            sequenceName = "servicesSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "servicesSequence"
    )
    private Long servicesId;
    private String protocol;
    private String product;
    private String method;
    private String name;
   // private String cpe;

    private int conf;
    private String OStype;
    private int version;
    private int portID;

}
