package org.example.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Area;
import org.example.repository.AreaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

class AreaServiceTest {

    @Mock
    private AreaRepository areaRepository;

    @InjectMocks
    private AreaService areaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveArea() {
        Area area = new Area(100.0);
        when(areaRepository.saveArea(any(Area.class))).thenReturn(area);

        Area savedArea = areaService.saveArea(100.0);

        assertNotNull(savedArea);
        assertEquals(100.0, savedArea.getArea());
        verify(areaRepository).saveArea(any(Area.class));
    }

    @Test
    void testGetAllAreas() {
        Area area = new Area(100.0);
        when(areaRepository.getAllAreas()).thenReturn(Collections.singletonList(area));

        List<Area> areas = areaService.getAllAreas();

        assertNotNull(areas);
        assertEquals(1, areas.size());
        assertEquals(100.0, areas.get(0).getArea());
        verify(areaRepository).getAllAreas();
    }
}
