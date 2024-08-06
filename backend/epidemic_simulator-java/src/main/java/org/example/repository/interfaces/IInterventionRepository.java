package org.example.repository.interfaces;

import org.example.domain.Intervention;

import java.util.List;

public interface IInterventionRepository {

    Intervention saveIntervention(Intervention intervention);
    List<Intervention> getInterventionsBySimulation(Long simulation_id);

}
