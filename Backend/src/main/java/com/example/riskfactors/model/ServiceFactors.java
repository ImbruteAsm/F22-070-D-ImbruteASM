package com.example.riskfactors.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceFactors {

    @Id
    @SequenceGenerator(
            name = "serviceFactorsSequence",
            sequenceName = "serviceFactorsSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "serviceFactorsSequence"
    )
    private Long ServiceFactorsId;
    private Boolean mongoDiscovered;
    private Boolean cassandraDiscovered;
    private Boolean msSQLDiscovered;
    private Boolean mySQLDiscovered;
    private Boolean redisDiscovered;
    private Boolean vncDiscovered;
    private Boolean rdpDiscovered;
    private Boolean rsyncDiscovered;
    private Boolean imapDiscovered;
    private Boolean ftpDiscovered;
    private Boolean smbDiscovered;
    private Boolean telnetDiscovered;
    private Boolean pop3Discovered;
    @ElementCollection
    private Map<String, Boolean> serviceDict;
    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "serviceFactorsFK"
    )
    private List<Port> ports;

}
