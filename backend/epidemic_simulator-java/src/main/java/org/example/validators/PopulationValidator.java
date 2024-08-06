package org.example.validators;

import lombok.AllArgsConstructor;
import org.example.domain.Population;
import org.example.domain.Simulation;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PopulationValidator {

    private boolean isValidPopulationSize(Integer populationSize) {
        return ( populationSize <= 4000000 && populationSize >= 255);
    }

    private boolean isValidInitiallyInfected(Integer initiallyInfected, Integer populationSize) {
        return ( initiallyInfected < populationSize);
    }

    public void validatePopulation(Population population) throws Exception {
        if(!isValidPopulationSize(population.getPopulationSize())) {
            throw new Exception("PopulationSize is not valid");
        }

        if(!isValidInitiallyInfected(population.getInitiallyInfected(), population.getPopulationSize())) {
            throw new Exception("InitiallyInfected is not valid");
        }
    }
}
