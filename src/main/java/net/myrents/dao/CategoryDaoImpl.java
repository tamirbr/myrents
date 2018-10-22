package net.myrents.dao;

import net.myrents.model.Category;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Category> findAll(String type) {
//        Session session = sessionFactory.openSession();
//        Criteria criteria = session.createCriteria(Category.class);
//        criteria.add( Restrictions.eq("type", type));
//        List<Category> categories = criteria.list();
//        session.close();
        Session session = sessionFactory.openSession();
        String hql = "from Category where type = '"+type+"' order by name";
        Query query = session.createQuery(hql);
        List<Category> categories = query.list();
        session.close();
        return categories;
    }

    @Override
    public List<String> findAllGroupTypes() {
        Session session = sessionFactory.openSession();
        String hql = "select DISTINCT type FROM Category";
        Query query = session.createQuery(hql);
        List<String> groupTypes = query.list();
        session.close();
        return groupTypes;
    }

    @Override
    public Category findById(Long id) {
        Session session = sessionFactory.openSession();
        Category category = session.get(Category.class,id);
        //Hibernate.initialize(category.getSubCategories());
        session.close();
        return category;
    }

    @Override
    public void save(Category category) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(category);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Category category) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(category);
        session.getTransaction().commit();
        session.close();
    }
}
