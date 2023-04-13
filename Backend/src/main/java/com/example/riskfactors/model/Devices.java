package com.example.riskfactors.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;
@Setter
@Getter
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Devices {

    @Id
    @SequenceGenerator(
            name = "devicesSequence",
            sequenceName = "devicesSequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "devicesSequence"
    )
    private Long deviceId;


    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "devicesFK"
    )
    private List<Hostname> hosts;
    private int total;
    private int up;
    private int down;
    private String addrType;
    private String addr;


}
