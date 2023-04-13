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
public class Port {

    @Id
    @SequenceGenerator(
            name = "portSequence",
            sequenceName = "portSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "portSequence"
    )
    private Long portId;
    private String name;
    private String port;
    private String state;

    public Port(String name, String port, String state) {
        this.name = name;
        this.port = port;
        this.state = state;
    }
}
