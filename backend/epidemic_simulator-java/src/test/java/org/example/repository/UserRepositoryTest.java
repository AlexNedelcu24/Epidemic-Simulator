package org.example.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.Collections;
import java.util.List;

class UserRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserRepository userRepository;

    @Mock
    private Session session;

    @Mock
    private Query<User> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
    }

    @Test
    void testGetAllUsers() {
        User user = new User("username", "email", "password", true, true);
        when(session.createQuery("from User", User.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.singletonList(user));

        List<User> users = userRepository.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
        verify(session).createQuery("from User", User.class);
        verify(query).getResultList();
    }

    @Test
    void testGetUserByUsername() {
        User user = new User("username", "email", "password", true, true);
        when(session.createQuery("from User where username = :username", User.class)).thenReturn(query);
        when(query.setParameter("username", "username")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        User foundUser = userRepository.getUserByUsername("username");

        assertEquals(user, foundUser);
        verify(session).createQuery("from User where username = :username", User.class);
        verify(query).setParameter("username", "username");
        verify(query).uniqueResult();
    }

    @Test
    void testGetUserById() {
        User user = new User("username", "email", "password", true, true);
        when(session.createQuery("from User where id = :id", User.class)).thenReturn(query);
        when(query.setParameter("id", 1L)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        User foundUser = userRepository.getUserById(1L);

        assertEquals(user, foundUser);
        verify(session).createQuery("from User where id = :id", User.class);
        verify(query).setParameter("id", 1L);
        verify(query).uniqueResult();
    }

    @Test
    void testGetUserByEmail() {
        User user = new User("username", "email", "password", true, true);
        when(session.createQuery("from User where email = :email", User.class)).thenReturn(query);
        when(query.setParameter("email", "email")).thenReturn(query);
        when(query.uniqueResult()).thenReturn(user);

        User foundUser = userRepository.getUserByEmail("email");

        assertEquals(user, foundUser);
        verify(session).createQuery("from User where email = :email", User.class);
        verify(query).setParameter("email", "email");
        verify(query).uniqueResult();
    }
}


