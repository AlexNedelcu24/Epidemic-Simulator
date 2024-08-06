package org.example.repository.interfaces;

import org.example.domain.Area;

import java.util.List;

public interface IAreaRepository {

    List<Area> getAllAreas();
    Area saveArea(Area area);

}
