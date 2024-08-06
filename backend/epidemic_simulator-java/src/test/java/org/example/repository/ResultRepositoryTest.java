package org.example.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Result;
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

class ResultRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ResultRepository resultRepository;

    @Mock
    private Session session;

    @Mock
    private Query<Result> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.createQuery("from Result r where r.simulationId = :simulation_id", Result.class)).thenReturn(query);
    }

    @Test
    void testSaveResult() {
        Result result = new Result(1, 100, 1L);
        when(entityManager.merge(result)).thenReturn(result);

        Result savedResult = resultRepository.saveResult(result);

        assertEquals(result, savedResult);
        verify(entityManager).merge(result);
    }

    @Test
    void testGetResultsBySimulation() {
        Result result = new Result(1, 100, 1L);
        when(query.setParameter("simulation_id", 1L)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(result));

        List<Result> results = resultRepository.getResultsBySimulation(1L);

        assertEquals(1, results.size());
        assertEquals(result, results.get(0));
        verify(session).createQuery("from Result r where r.simulationId = :simulation_id", Result.class);
        verify(query).setParameter("simulation_id", 1L);
        verify(query).getResultList();
    }
}
