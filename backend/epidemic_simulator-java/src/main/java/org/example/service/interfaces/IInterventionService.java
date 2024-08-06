package org.example.service.interfaces;

import org.example.domain.Intervention;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface IInterventionService {
    Intervention saveIntervention(int countryId, String countryName, String name, int value, Long simulationId) throws ValidationException;
    List<Intervention> getInterventionBySimulation(Long simulationId);
}
