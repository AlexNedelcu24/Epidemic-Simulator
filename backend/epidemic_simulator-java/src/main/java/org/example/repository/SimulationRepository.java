package org.example.repository;

import org.example.domain.Page;
import org.example.domain.Population;
import org.example.domain.Simulation;
import org.example.domain.User;
import org.example.repository.interfaces.ISimulationRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Repository
public class SimulationRepository implements ISimulationRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Add simulation in DB ( true -> added / false -> no)
     *
     * @param simulation
     */
    @Transactional
    public Simulation saveSimulation(Simulation simulation) {
        return entityManager.merge(simulation);
    }

    /**
     * Returns all simulations for one user
     *
     * @param user_id
     * @return user's simulation
     */
    public List<Simulation> getSimulationsByUser(Long user_id) {
        Session session = entityManager.unwrap(Session.class);
        String hql = "from Simulation s where s.userId = :user_id";
        List<Simulation> simulations = session.createQuery(hql, Simulation.class)
                .setParameter("user_id", user_id)
                .getResultList();
        return simulations != null ? simulations : Collections.emptyList();
    }

    /**
     * Returns a simulation
     *
     * @param name
     * @return a simulation
     */
    public Simulation getSimulationByName(String name) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Simulation where name = :name", Simulation.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    public Simulation getSimulationById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Simulation where id = :id", Simulation.class)
                .setParameter("id", id)
                .uniqueResult();
    }
}
