package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.Intervention;
import org.example.domain.Population;
import org.example.domain.Simulation;
import org.example.repository.InterventionRepository;
import org.example.repository.SimulationRepository;
import org.example.repository.interfaces.IInterventionRepository;
import org.example.repository.interfaces.ISimulationRepository;
import org.example.service.interfaces.IInterventionService;
import org.example.validators.InterventionValidator;

import javax.xml.bind.ValidationException;
import java.util.List;

@org.springframework.stereotype.Service(value = "interventionService")
@AllArgsConstructor
public class InterventionService implements IInterventionService {

    private final InterventionValidator interventionValidator;
    private final IInterventionRepository interventionRepository;
    private final ISimulationRepository simulationRepository;

    public Intervention saveIntervention(int countryId, String countryName, String name, int value, Long simulationId) throws ValidationException {
        try {
            Intervention intervention = new Intervention(countryId, countryName, name, value, simulationId);
            interventionValidator.validateIntervention(intervention);
            if (simulationRepository.getSimulationById(simulationId) == null) {
                throw new ValidationException("There is no simulation for this intervention!");
            }

            return interventionRepository.saveIntervention(intervention);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }


    public List<Intervention> getInterventionBySimulation(Long simulationId){
        return interventionRepository.getInterventionsBySimulation(simulationId);
    }
}
