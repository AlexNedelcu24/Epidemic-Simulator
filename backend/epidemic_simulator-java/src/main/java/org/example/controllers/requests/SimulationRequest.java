package org.example.controllers.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimulationRequest {

    private Long userId;
    private String name;
    private int populationSize;
    private int initiallyInfected;
    private int minContacts;
    private int maxContacts;
    private int minIncubationPeriod;
    private int maxIncubationPeriod;
    private int minInfectiousPeriod;
    private int maxInfectiousPeriod;
    private int transmissionProbability;

}

