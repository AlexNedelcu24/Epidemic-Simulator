package org.example.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Intervention;
import org.example.domain.Simulation;
import org.example.repository.InterventionRepository;
import org.example.repository.SimulationRepository;
import org.example.validators.InterventionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.bind.ValidationException;
import java.util.Collections;
import java.util.List;

class InterventionServiceTest {

    @Mock
    private InterventionRepository interventionRepository;

    @Mock
    private SimulationRepository simulationRepository;

    @Mock
    private InterventionValidator interventionValidator;

    @InjectMocks
    private InterventionService interventionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveIntervention() throws ValidationException {
        Intervention intervention = new Intervention(1, "Country", "Name", 100, 1L);
        when(simulationRepository.getSimulationById(anyLong())).thenReturn(new Simulation());
        when(interventionRepository.saveIntervention(any(Intervention.class))).thenReturn(intervention);

        Intervention savedIntervention = interventionService.saveIntervention(1, "Country", "Name", 100, 1L);

        assertNotNull(savedIntervention);
        assertEquals("Country", savedIntervention.getCountryName());
        verify(interventionRepository).saveIntervention(any(Intervention.class));
    }

    @Test
    void testGetInterventionBySimulation() {
        Intervention intervention = new Intervention(1, "Country", "Name", 100, 1L);
        when(interventionRepository.getInterventionsBySimulation(anyLong())).thenReturn(Collections.singletonList(intervention));

        List<Intervention> interventions = interventionService.getInterventionBySimulation(1L);

        assertNotNull(interventions);
        assertEquals(1, interventions.size());
        assertEquals("Country", interventions.get(0).getCountryName());
        verify(interventionRepository).getInterventionsBySimulation(anyLong());
    }
}
