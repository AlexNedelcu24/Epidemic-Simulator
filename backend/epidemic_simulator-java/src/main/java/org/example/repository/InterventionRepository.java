package org.example.repository;

import org.example.domain.Intervention;
import org.example.domain.Population;
import org.example.domain.Simulation;
import org.example.repository.interfaces.IInterventionRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Repository
public class InterventionRepository implements IInterventionRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Intervention saveIntervention(Intervention intervention) {
        return entityManager.merge(intervention);
    }


    public List<Intervention> getInterventionsBySimulation(Long simulation_id) {
        Session session = entityManager.unwrap(Session.class);
        String hql = "from Intervention i where i.simulationId = :simulation_id";
        List<Intervention> interventions = session.createQuery(hql, Intervention.class)
                .setParameter("simulation_id", simulation_id)
                .getResultList();
        return interventions != null ? interventions : Collections.emptyList();
    }
}
