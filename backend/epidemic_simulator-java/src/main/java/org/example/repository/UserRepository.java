package org.example.repository;

import org.example.domain.User;
import org.example.repository.interfaces.IUserRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepository implements IUserRepository {

    @Autowired
    private EntityManager entityManager;

    /**
     * Returns all users from DB
     *
     * @return all users
     */
    public List<User> getAllUsers() {
        Session session = entityManager.unwrap(Session.class);
        List<User> users = session.createQuery("from User", User.class).getResultList();
        return users != null ? users : Collections.emptyList();
    }

    /**
     * Saves a user to the database.
     *
     * @param user the user to be saved
     */
    public void saveUser(User user) {
        Session session = entityManager.unwrap(Session.class);
        if (user.getId() == null) {
            session.save(user);
        } else {
            session.saveOrUpdate(user);
        }
    }

    @Transactional
    public void updateUser(User newUser, User oldUser) {
        Session session = entityManager.unwrap(Session.class);
        String updateSql = "update User u " +
                "set u.hasPaused = :newHasPaused, " +
                " u.infectionsNumber = :newInfectionsNumber " +
                "where u.id = :oldUserId";
        session.createQuery(updateSql)
                .setParameter("newHasPaused", newUser.getHasPaused())
                .setParameter("newInfectionsNumber", newUser.getInfectionsNumber())
                .setParameter("oldUserId", oldUser.getId())
                .executeUpdate();
    }

    /**
     * Returns user by username / null (if not found)
     *
     * @param username
     * @return one usern or null
     */
    public User getUserByUsername(String username) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from User where username = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();
    }

    /**
     * Returns user by id / null (if not found)
     *
     * @param id
     * @return one user or null
     */
    public User getUserById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from User where id = :id", User.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    /**
     * Returns user by email / null (if not found)
     *
     * @param email
     * @return one username or null
     */
    public User getUserByEmail(String email) {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("from User where email = :email", User.class)
                .setParameter("email", email)
                .uniqueResult();
    }
}
