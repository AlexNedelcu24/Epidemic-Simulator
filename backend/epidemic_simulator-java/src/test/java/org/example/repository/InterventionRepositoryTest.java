package org.example.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Intervention;
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

class InterventionRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private InterventionRepository interventionRepository;

    @Mock
    private Session session;

    @Mock
    private Query<Intervention> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    void testSaveIntervention() {
        Intervention intervention = new Intervention(1, "Country", "Intervention", 100, 1L);
        when(entityManager.merge(intervention)).thenReturn(intervention);

        Intervention savedIntervention = interventionRepository.saveIntervention(intervention);

        assertEquals(intervention, savedIntervention);
        verify(entityManager).merge(intervention);
    }

    @Test
    void testGetInterventionsBySimulation() {
        Intervention intervention = new Intervention(1, "Country", "Intervention", 100, 1L);
        when(session.createQuery("from Intervention i where i.simulationId = :simulation_id", Intervention.class)).thenReturn(query);
        when(query.setParameter("simulation_id", 1L)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(intervention));

        List<Intervention> interventions = interventionRepository.getInterventionsBySimulation(1L);

        assertEquals(1, interventions.size());
        assertEquals(intervention, interventions.get(0));
        verify(session).createQuery("from Intervention i where i.simulationId = :simulation_id", Intervention.class);
        verify(query).setParameter("simulation_id", 1L);
        verify(query).getResultList();
    }
}
