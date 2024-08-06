package org.example.repository;

import org.example.domain.Intervention;
import org.example.domain.Population;
import org.example.domain.Result;
import org.example.repository.interfaces.IResultsRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Repository
public class ResultRepository implements IResultsRepository {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Result saveResult(Result result) {
        return entityManager.merge(result);
    }


    public List<Result> getResultsBySimulation(Long simulation_id) {
        Session session = entityManager.unwrap(Session.class);
        String hql = "from Result r where r.simulationId = :simulation_id";
        List<Result> results = session.createQuery(hql, Result.class)
                .setParameter("simulation_id", simulation_id)
                .getResultList();
        return results != null ? results : Collections.emptyList();
    }
}
