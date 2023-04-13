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
public class EndpointSecurity {

    @Id
    @SequenceGenerator(
            name = "endPointSecuritySequence",
            sequenceName = "endPointSecuritySequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "endPointSecuritySequence"
    )
    private Long endPointSecurityId;
    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "servicesFK"
    )
    private List<Services> endpointServices;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "endPointSecurityFK"
    )
    private List<OperatingSystem> endpointOperatingSystem;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "devicesFK"
    )
    private Devices endpointDevices;

    public EndpointSecurity(List<OperatingSystem> endpointOS, Devices endpointDevices, List<Services> endpointServices){
        this.endpointServices = endpointServices;
        this.endpointOperatingSystem= endpointOS;
        this.endpointDevices = endpointDevices;

    }


}
