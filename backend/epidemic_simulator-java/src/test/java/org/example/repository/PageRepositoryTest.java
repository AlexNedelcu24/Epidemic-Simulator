package org.example.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javax.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.query.Query;

class PageRepositoryTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private PageRepository pageRepository;

    @Mock
    private Session session;

    @Mock
    private Query<Page> query;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.unwrap(Session.class)).thenReturn(session);
        when(session.createQuery("from Page where id = :id", Page.class)).thenReturn(query);
    }

    @Test
    void testGetPageById() {
        Page page = new Page("Title", "Text", "Link");
        when(query.setParameter("id", 1L)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(page);

        Page foundPage = pageRepository.getPageById(1L);

        assertEquals(page, foundPage);
        verify(session).createQuery("from Page where id = :id", Page.class);
        verify(query).setParameter("id", 1L);
        verify(query).uniqueResult();
    }

    @Test
    void testSavePage() {
        Page page = new Page("Title", "Text", "Link");
        when(entityManager.merge(page)).thenReturn(page);

        Page savedPage = pageRepository.savePage(page);

        assertEquals(page, savedPage);
        verify(entityManager).merge(page);
    }
}

