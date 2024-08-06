package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.Area;
import org.example.repository.AreaRepository;
import org.example.repository.UserRepository;
import org.example.repository.interfaces.IAreaRepository;
import org.example.service.interfaces.IAreaService;

import java.util.List;

@org.springframework.stereotype.Service(value = "areaService")
@AllArgsConstructor
public class AreaService implements IAreaService {

    private final IAreaRepository areaRepository;

    public Area saveArea(double area) {
        Area a = new Area(area);
        return areaRepository.saveArea(a);
    }

    public List<Area> getAllAreas() {
        return areaRepository.getAllAreas();
    }
}
