package org.example.service;

import lombok.AllArgsConstructor;
import org.example.domain.Area;
import org.example.domain.Page;
import org.example.repository.AreaRepository;
import org.example.repository.PageRepository;
import org.example.repository.interfaces.IPageRepository;
import org.example.service.interfaces.IPageService;

@org.springframework.stereotype.Service(value = "pageService")
@AllArgsConstructor
public class PageService implements IPageService {

    private final IPageRepository pageRepository;

    public Page savePage(String title, String text, String link) {
        Page p = new Page(title, text, link);
        return pageRepository.savePage(p);
    }

    public Page getPage(Long id) {
        return pageRepository.getPageById(id);
    }
}
