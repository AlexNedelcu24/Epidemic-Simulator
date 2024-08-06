package org.example.repository;

import org.example.domain.Intervention;
import org.example.domain.Population;
import org.example.domain.Simulation;
import org.example.domain.User;
import org.example.repository.interfaces.IPopulationRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Repository
public class PopulationRepository implements IPopulationRepository {

    @Autowired
    private EntityManager entityManager;


    @Transactional
    public Population savePopulation(Population population) {
        return entityManager.merge(population);
    }

    public Population getPopulationBySimulation(Long simulation_id) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Population where simulationId = :simulation_id", Population.class)
                .setParameter("simulation_id", simulation_id)
                .uniqueResult();
    }



}
