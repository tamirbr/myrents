package net.myrents.dao;

import net.myrents.model.Item;
import net.myrents.model.User;
import net.myrents.model.Utils;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.openSession();
        List<User> users = session.createCriteria(User.class).list();
        session.close();
        return users;
    }

    @Override
    public User findById(Long id) {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class,id);
        Hibernate.initialize(user.getItems());
        Hibernate.initialize(user.getBookMarks());
        session.close();
        return user;
    }

    @Override
    public User findByIdActiveItems(Long id) {
        Session session = sessionFactory.openSession();
        User user = session.get(User.class,id);
        String hql = "from Item where active is true and user_id = "+id;
        Query query = session.createQuery(hql);
        user.setItems(query.list());
        session.close();
        return user;
    }

    @Override
    public User findByEmail(String email) {
        Session session = sessionFactory.openSession();
        User user = new User();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add( Restrictions.eq("email", email));
        List results = criteria.list();
        if(!results.isEmpty()) {
            user = (User) results.get(0);
            Hibernate.initialize(user.getItems());
            Hibernate.initialize(user.getBookMarks());
        } else{
            user = null;
        }
        session.close();
        return user;
    }

    @Override
    public void save(User user) {
        user.setImage(Utils.cropImage(Utils.sizeImage(user.getImage())));
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }
}
