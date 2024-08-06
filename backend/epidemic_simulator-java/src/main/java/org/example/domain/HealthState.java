package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "healtstates")
@AllArgsConstructor
public class HealthState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private int exposureDay;
    private int infectionDay;
    private int recoveryDay;

    public HealthState() {

    }

    public HealthState(int exposureDay, int infectionDay, int recoveryDay) {
        this.exposureDay = exposureDay;
        this.infectionDay = infectionDay;
        this.recoveryDay = recoveryDay;
    }
}
