package org.example.service.interfaces;

import org.example.domain.Human;
import org.example.domain.Simulation;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ISimulationService {

    Simulation saveSimulation(Long userId, String name, int transmissionProbability) throws ValidationException;
    Simulation createSimulation(Long userId, String name, int transmissionProbability) throws ValidationException;
    List<Simulation> getUsersSimulations(Long userId);
    void startSimulation(Simulation simulation, List<Human> humans) throws ValidationException;
    void pauseSimulation(Long id);
    void continueSimulation(Long id);
    List<Double> getInfectionsNumberPerCountry(Long id, List<Integer> populationList);
    void setInfections(Long id);
    void addToDistanceMap(Long userId, Integer countryId, Integer days);
    void addToVaccinMap(Long userId, Integer countryId, Integer percent);
    void startParallel(Simulation simulation, List<Human> humans) throws ExecutionException, InterruptedException;
    Simulation getSimulationById(Long id);

}
