package net.myrents.dao;

import net.myrents.model.Category;
import net.myrents.model.SubCategory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubCategoryDaoImpl implements SubCategoryDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<SubCategory> findAll(Long id) {
//        Session session = sessionFactory.openSession();
//        Criteria criteria = session.createCriteria(SubCategory.class);
//        criteria.add( Restrictions.eq("category_id", id));
//        List<SubCategory> subCategories = criteria.list();
//        session.close();
        Session session = sessionFactory.openSession();
        String hql = "FROM SubCategory WHERE category_id = '"+id+"' ORDER BY name";
        Query query = session.createQuery(hql);
        List<SubCategory> subCategories = query.list();
        session.close();
        return subCategories;
    }

    @Override
    public SubCategory findById(Long id) {
        Session session = sessionFactory.openSession();
        SubCategory subCategory = session.get(SubCategory.class,id);
        //Hibernate.initialize(category.getSubCategories());
        session.close();
        return subCategory;
    }

    @Override
    public void save(SubCategory subCategory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(subCategory);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(SubCategory subCategory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(subCategory);
        session.getTransaction().commit();
        session.close();
    }
}












































































































































































