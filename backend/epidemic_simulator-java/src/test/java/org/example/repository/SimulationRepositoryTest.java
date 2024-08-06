package org.example.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Simulation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.Collections;
import java.util.List;

class SimulationRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private SimulationRepository simulationRepository;

    @Mock
    private Session session;

    @Mock
    private Query<Simulation> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    void testSaveSimulation() {
        Simulation simulation = new Simulation(1L, "Simulation", 10);
        when(entityManager.merge(simulation)).thenReturn(simulation);

        Simulation savedSimulation = simulationRepository.saveSimulation(simulation);

        assertEquals(simulation, savedSimulation);
        verify(entityManager).merge(simulation);
    }

    @Test
    void testGetSimulationsByUser() {
        Simulation simulation = new Simulation(1L, "Simulation", 10);
        when(session.createQuery("from Simulation s where s.userId = :user_id", Simulation.class)).thenReturn(query);
        when(query.setParameter("user_id", 1L)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(simulation));

        List<Simulation> simulations = simulationRepository.getSimulationsByUser(1L);

        assertEquals(1, simulations.size());
        assertEquals(simulation, simulations.get(0));
        verify(session).createQuery("from Simulation s where s.userId = :user_id", Simulation.class);
        verify(query).setParameter("user_id", 1L);
        verify(query).getResultList();
    }

    @Test
    void testGetSimulationByName() {
        Simulation simulation = new Simulation(1L, "Simulation", 10);
        when(session.createQuery("from Simulation where name = :name", Simulation.class)).thenReturn(query);
        when(query.setParameter("name", "Simulation")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(simulation);

        Simulation foundSimulation = simulationRepository.getSimulationByName("Simulation");

        assertEquals(simulation, foundSimulation);
        verify(session).createQuery("from Simulation where name = :name", Simulation.class);
        verify(query).setParameter("name", "Simulation");
        verify(query).uniqueResult();
    }

    @Test
    void testGetSimulationById() {
        Simulation simulation = new Simulation(1L, "Simulation", 10);
        when(session.createQuery("from Simulation where id = :id", Simulation.class)).thenReturn(query);
        when(query.setParameter("id", 1L)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(simulation);

        Simulation foundSimulation = simulationRepository.getSimulationById(1L);

        assertEquals(simulation, foundSimulation);
        verify(session).createQuery("from Simulation where id = :id", Simulation.class);
        verify(query).setParameter("id", 1L);
        verify(query).uniqueResult();
    }
}
