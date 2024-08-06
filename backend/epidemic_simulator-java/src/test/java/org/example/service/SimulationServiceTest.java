package org.example.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Simulation;
import org.example.domain.User;
import org.example.repository.SimulationRepository;
import org.example.repository.UserRepository;
import org.example.validators.SimulationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.bind.ValidationException;
import java.util.Collections;
import java.util.List;

class SimulationServiceTest {

    @Mock
    private SimulationRepository simulationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ResultService resultService;

    @Mock
    private SimulationValidator simulationValidator;

    @InjectMocks
    private SimulationService simulationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveSimulation() throws ValidationException {
        Simulation simulation = new Simulation(1L, "Simulation1", 50);
        when(userRepository.getUserById(anyLong())).thenReturn(new User());
        when(simulationRepository.saveSimulation(any(Simulation.class))).thenReturn(simulation);

        Simulation savedSimulation = simulationService.saveSimulation(1L, "Simulation1", 50);

        assertNotNull(savedSimulation);
        assertEquals("Simulation1", savedSimulation.getName());
        verify(simulationRepository).saveSimulation(any(Simulation.class));
    }

    @Test
    void testGetUsersSimulations() {
        Simulation simulation = new Simulation(1L, "Simulation1", 50);
        when(simulationRepository.getSimulationsByUser(anyLong())).thenReturn(Collections.singletonList(simulation));

        List<Simulation> simulations = simulationService.getUsersSimulations(1L);

        assertNotNull(simulations);
        assertEquals(1, simulations.size());
        assertEquals("Simulation1", simulations.get(0).getName());
        verify(simulationRepository).getSimulationsByUser(anyLong());
    }

    @Test
    void testGetSimulationById() {
        Simulation simulation = new Simulation(1L, "Simulation1", 50);
        when(simulationRepository.getSimulationById(anyLong())).thenReturn(simulation);

        Simulation foundSimulation = simulationService.getSimulationById(1L);

        assertNotNull(foundSimulation);
        assertEquals("Simulation1", foundSimulation.getName());
        verify(simulationRepository).getSimulationById(anyLong());
    }
}
