package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "interventions")
@AllArgsConstructor
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private int countryId;
    private String countryName;
    private String name;
    private int value;
    private Long simulationId;


    public Intervention(int countryId, String countryName, String name, int value, Long simulationId) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.name = name;
        this.value = value;
        this.simulationId = simulationId;
    }

    public Intervention() {

    }
}
