package net.myrents.dao;

import net.myrents.model.BookMark;


public interface BookMarkDao {
    BookMark findById(Long id);
    void save(BookMark bookMark);
    void delete(BookMark bookMark);
}
