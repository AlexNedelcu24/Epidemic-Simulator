package org.example.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Area;
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

class AreaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private AreaRepository areaRepository;

    @Mock
    private Session session;

    @Mock
    private Query<Area> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    void testGetAllAreas() {
        Area area = new Area(1.0);
        when(session.createQuery("from Area", Area.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(area));

        List<Area> areas = areaRepository.getAllAreas();

        assertEquals(1, areas.size());
        assertEquals(area, areas.get(0));
        verify(session).createQuery("from Area", Area.class);
        verify(query).getResultList();
    }

    @Test
    void testSaveArea() {
        Area area = new Area(1.0);
        when(entityManager.merge(area)).thenReturn(area);

        Area savedArea = areaRepository.saveArea(area);

        assertEquals(area, savedArea);
        verify(entityManager).merge(area);
    }
}

