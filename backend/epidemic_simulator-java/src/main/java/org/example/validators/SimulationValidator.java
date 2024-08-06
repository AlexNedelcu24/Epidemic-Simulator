package org.example.validators;

import lombok.AllArgsConstructor;
import org.example.domain.Simulation;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SimulationValidator {

    private boolean isValidTransmissionProbability(Integer transmissionProbability) {
        return ( transmissionProbability <= 100 && transmissionProbability >= 1);
    }

    public void validateSimulation(Simulation simulation) throws Exception {
        if(!isValidTransmissionProbability(simulation.getTransmissionProbability())) {
            throw new Exception("TransmissionProbability is not valid");
        }
    }

}
