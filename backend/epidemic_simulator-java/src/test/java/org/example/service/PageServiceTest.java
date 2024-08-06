package org.example.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.domain.Page;
import org.example.repository.PageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class PageServiceTest {

    @Mock
    private PageRepository pageRepository;

    @InjectMocks
    private PageService pageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePage() {
        Page page = new Page("Title", "Text", "Link");
        when(pageRepository.savePage(any(Page.class))).thenReturn(page);

        Page savedPage = pageService.savePage("Title", "Text", "Link");

        assertNotNull(savedPage);
        assertEquals("Title", savedPage.getTitle());
        verify(pageRepository).savePage(any(Page.class));
    }

    @Test
    void testGetPage() {
        Page page = new Page("Title", "Text", "Link");
        when(pageRepository.getPageById(anyLong())).thenReturn(page);

        Page foundPage = pageService.getPage(1L);

        assertNotNull(foundPage);
        assertEquals("Title", foundPage.getTitle());
        verify(pageRepository).getPageById(anyLong());
    }
}
