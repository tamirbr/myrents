package net.myrents.service;

import net.myrents.dao.BookMarkDao;
import net.myrents.dao.ItemDao;
import net.myrents.model.BookMark;
import net.myrents.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookMarkServiceImpl implements BookMarkService {
    @Autowired
    private BookMarkDao bookMarkDao;

    @Override
    public BookMark findById(Long id) {
        return bookMarkDao.findById(id);
    }

    @Override
    public void save(BookMark bookMark) {
        bookMarkDao.save(bookMark);
    }

    @Override
    public void delete(BookMark bookMark) {
        bookMarkDao.delete(bookMark);
    }
}
