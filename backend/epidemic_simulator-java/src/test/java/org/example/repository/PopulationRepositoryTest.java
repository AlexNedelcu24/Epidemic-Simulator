package org.example.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Population;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;

class PopulationRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PopulationRepository populationRepository;

    @Mock
    private Session session;

    @Mock
    private Query<Population> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.createQuery("from Population where simulationId = :simulation_id", Population.class)).thenReturn(query);
    }

    @Test
    void testSavePopulation() {
        Population population = new Population(1000, 10, 2, 5, 1, 14, 1, 14, 1L);
        when(entityManager.merge(population)).thenReturn(population);

        Population savedPopulation = populationRepository.savePopulation(population);

        assertEquals(population, savedPopulation);
        verify(entityManager).merge(population);
    }

    @Test
    void testGetPopulationBySimulation() {
        Population population = new Population(1000, 10, 2, 5, 1, 14, 1, 14, 1L);
        when(query.setParameter("simulation_id", 1L)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(population);

        Population foundPopulation = populationRepository.getPopulationBySimulation(1L);

        assertEquals(population, foundPopulation);
        verify(session).createQuery("from Population where simulationId = :simulation_id", Population.class);
        verify(query).setParameter("simulation_id", 1L);
        verify(query).uniqueResult();
    }
}

