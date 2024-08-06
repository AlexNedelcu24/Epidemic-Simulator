package org.example.validators;

import org.example.domain.Simulation;
import org.example.validators.SimulationValidator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimulationValidatorTest {

    private final SimulationValidator simulationValidator = new SimulationValidator();

    @Test
    void validateSimulation_ValidTransmissionProbability_DoesNotThrowException() {
        Simulation simulation = new Simulation(1L, "SimulationName", 50);
        assertDoesNotThrow(() -> simulationValidator.validateSimulation(simulation));
    }

    @Test
    void validateSimulation_InvalidTransmissionProbability_ThrowsException() {
        Simulation simulation = new Simulation(1L, "SimulationName", 150);
        Exception exception = assertThrows(Exception.class, () -> simulationValidator.validateSimulation(simulation));
        assertEquals("TransmissionProbability is not valid", exception.getMessage());
    }
}
