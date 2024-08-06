package org.example.service.interfaces;

import org.example.domain.Page;

public interface IPageService {

    Page savePage(String title, String text, String link);

    Page getPage(Long id);

}
