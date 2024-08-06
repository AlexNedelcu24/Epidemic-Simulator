package org.example.repository.interfaces;

import org.example.domain.Simulation;

import java.util.List;

public interface ISimulationRepository {

    Simulation saveSimulation(Simulation simulation);
    List<Simulation> getSimulationsByUser(Long user_id);
    Simulation getSimulationByName(String name);
    Simulation getSimulationById(Long id);

}
