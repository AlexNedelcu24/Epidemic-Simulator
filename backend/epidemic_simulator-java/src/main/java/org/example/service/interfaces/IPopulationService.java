package org.example.service.interfaces;

import org.example.domain.Human;
import org.example.domain.Population;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface IPopulationService {

    Population savePopulation(int populationSize, int initiallyInfected, int minContacts, int maxContacts, int minIncubationPeriod, int maxIncubationPeriod, int minInfectiousPeriod, int maxInfectiousPeriod, Long simulationId) throws ValidationException;
    List<Human> initializePopulation(Population population, String simulationName);
    List<Integer> getPopulationByCountry(List<Human> humans);
    Population getPopulationBySimulation(Long simulationId);

}
