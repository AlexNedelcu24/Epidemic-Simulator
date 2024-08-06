package org.example.repository;

import org.example.domain.Area;
import org.example.domain.Page;
import org.example.domain.User;
import org.example.repository.interfaces.IAreaRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Repository
public class AreaRepository implements IAreaRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Returns all areas from DB
     *
     * @return all areas
     */
    public List<Area> getAllAreas() {
        Session session = entityManager.unwrap(Session.class);
        List<Area> areas = session.createQuery("from Area", Area.class).getResultList();
        return areas != null ? areas : Collections.emptyList();
    }

    /**
     * Saves an area to the database.
     *
     * @param area the user to be saved
     */
    @Transactional
    public Area saveArea(Area area) {
        return entityManager.merge(area);
    }


}
