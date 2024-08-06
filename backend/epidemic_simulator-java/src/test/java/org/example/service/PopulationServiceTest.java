package org.example.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Population;
import org.example.domain.Simulation;
import org.example.repository.AreaRepository;
import org.example.repository.PopulationRepository;
import org.example.repository.SimulationRepository;
import org.example.validators.PopulationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.bind.ValidationException;
import java.util.Collections;
import java.util.List;

class PopulationServiceTest {

    @Mock
    private PopulationRepository populationRepository;

    @Mock
    private SimulationRepository simulationRepository;

    @Mock
    private PopulationValidator populationValidator;

    @Mock
    private AreaRepository areaRepository;

    @InjectMocks
    private PopulationService populationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePopulation() throws ValidationException {
        Population population = new Population(1000, 10, 2, 10, 5, 14, 7, 21, 1L);
        when(simulationRepository.getSimulationById(anyLong())).thenReturn(new Simulation());
        when(populationRepository.savePopulation(any(Population.class))).thenReturn(population);

        Population savedPopulation = populationService.savePopulation(1000, 10, 2, 10, 5, 14, 7, 21, 1L);

        assertNotNull(savedPopulation);
        assertEquals(1000, savedPopulation.getPopulationSize());
        verify(populationRepository).savePopulation(any(Population.class));
    }

    @Test
    void testGetPopulationBySimulation() {
        Population population = new Population(1000, 10, 2, 10, 5, 14, 7, 21, 1L);
        when(populationRepository.getPopulationBySimulation(anyLong())).thenReturn(population);

        Population foundPopulation = populationService.getPopulationBySimulation(1L);

        assertNotNull(foundPopulation);
        assertEquals(1000, foundPopulation.getPopulationSize());
        verify(populationRepository).getPopulationBySimulation(anyLong());
    }
}
