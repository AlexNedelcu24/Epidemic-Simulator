package org.example.repository;

import org.example.domain.Page;
import org.example.domain.Simulation;
import org.example.domain.User;
import org.example.repository.interfaces.IPageRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class PageRepository implements IPageRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Returns a page
     *
     * @param id
     * @return a page
     */
    public Page getPageById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from Page where id = :id", Page.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Transactional
    public Page savePage(Page page) {
        return entityManager.merge(page);
    }


}
