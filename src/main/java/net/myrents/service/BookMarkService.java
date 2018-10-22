package net.myrents.service;

import net.myrents.model.BookMark;


public interface BookMarkService {
    BookMark findById(Long id);
    void save(BookMark bookMark);
    void delete(BookMark bookMark);
}
