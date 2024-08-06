package org.example.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Result;
import org.example.domain.Simulation;
import org.example.repository.ResultRepository;
import org.example.repository.SimulationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.xml.bind.ValidationException;
import java.util.Collections;
import java.util.List;

class ResultServiceTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private SimulationRepository simulationRepository;

    @InjectMocks
    private ResultService resultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveResult() throws ValidationException {
        Result result = new Result(1, 100, 1L);
        when(simulationRepository.getSimulationById(anyLong())).thenReturn(new Simulation());
        when(resultRepository.saveResult(any(Result.class))).thenReturn(result);

        Result savedResult = resultService.saveResult(1, 100, 1L);

        assertNotNull(savedResult);
        assertEquals(100, savedResult.getInfectionsNumber());
        verify(resultRepository).saveResult(any(Result.class));
    }

    @Test
    void testGetResultsBySimulation() {
        Result result = new Result(1, 100, 1L);
        when(resultRepository.getResultsBySimulation(anyLong())).thenReturn(Collections.singletonList(result));

        List<Result> results = resultService.getResultsBySimulation(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(100, results.get(0).getInfectionsNumber());
        verify(resultRepository).getResultsBySimulation(anyLong());
    }
}
