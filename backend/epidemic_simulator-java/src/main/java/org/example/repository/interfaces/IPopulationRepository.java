package org.example.repository.interfaces;

import org.example.domain.Population;

public interface IPopulationRepository {

    Population savePopulation(Population population);
    Population getPopulationBySimulation(Long simulation_id);

}
