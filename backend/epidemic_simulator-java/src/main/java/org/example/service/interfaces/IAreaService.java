package org.example.service.interfaces;

import org.example.domain.Area;

import java.util.List;

public interface IAreaService {
    Area saveArea(double area);
    List<Area> getAllAreas();
}
