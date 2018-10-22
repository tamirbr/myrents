package net.myrents.dao;

import net.myrents.model.BookMark;
import net.myrents.model.Item;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookMarkDaoImpl implements BookMarkDao {
    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public BookMark findById(Long id) {
        Session session = sessionFactory.openSession();
        BookMark bookMark = session.get(BookMark.class,id);
        session.close();
        return bookMark;
    }

    @Override
    public void save(BookMark bookMark) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(bookMark);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(BookMark bookMark) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(bookMark);
        session.getTransaction().commit();
        session.close();
    }
}
