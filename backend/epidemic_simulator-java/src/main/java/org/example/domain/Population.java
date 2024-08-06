package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "populations")
@AllArgsConstructor
public class Population {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private int populationSize;
    private int initiallyInfected;
    private int minContacts;
    private int maxContacts;
    private int minIncubationPeriod;
    private int maxIncubationPeriod;
    private int minInfectiousPeriod;
    private int maxInfectiousPeriod;
    private Long simulationId;

    public Population(int populationSize, int initiallyInfected, int minContacts, int maxContacts, int minIncubationPeriod, int maxIncubationPeriod, int minInfectiousPeriod, int maxInfectiousPeriod, Long simulationId) {
        this.populationSize = populationSize;
        this.initiallyInfected = initiallyInfected;
        this.minContacts = minContacts;
        this.maxContacts = maxContacts;
        this.minIncubationPeriod = minIncubationPeriod;
        this.maxIncubationPeriod = maxIncubationPeriod;
        this.minInfectiousPeriod = minInfectiousPeriod;
        this.maxInfectiousPeriod = maxInfectiousPeriod;
        this.simulationId = simulationId;
    }

    public Population() {
    }


}
