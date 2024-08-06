package org.example.repository.interfaces;

import org.example.domain.Page;

public interface IPageRepository {

    Page getPageById(Long id);
    Page savePage(Page page);

}
