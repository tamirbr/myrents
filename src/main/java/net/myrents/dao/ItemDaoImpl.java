package net.myrents.dao;

import net.myrents.model.ConstantsHe;
import net.myrents.model.Item;
import net.myrents.model.User;
import net.myrents.model.Utils;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemDaoImpl implements ItemDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Item> findAll(String groupType) {
        Session session = sessionFactory.openSession();
        String hql = "from Item where active is true and groupType = '"+groupType+"' order by postDate desc";
        Query query = session.createQuery(hql);
        List<Item> items = query.list();
        session.close();
        if(items.size() < ConstantsHe.NEW_ITEMS_AMOUNT){
            for(int i = items.size()-1 ;i < ConstantsHe.NEW_ITEMS_AMOUNT;i++){
                items.add(new Item());
            }
        }
        return items;
    }

    @Override
    public List<Item> searchAll(String sortBy,String priceStart, String priceEnd,String price,String groupType, Long cat, Long subCat) {
        Session session = sessionFactory.openSession();
        String hql;
        String searchPrice = "";
        String searchCat = "";
        String searchSubCat = "";

        if(!Utils.isEmptyOrNull(priceStart) && !Utils.isEmptyOrNull(priceEnd)){
            searchPrice = " and "+priceStart+" <= "+price+" and "+price+" <= "+priceEnd;
        }
        if(cat != 0){
            searchCat = " and category_id = "+cat;
        }
        if(subCat != 0){
            searchSubCat = " and subCategory_id = "+subCat;
        }

        if(sortBy.isEmpty() || sortBy.equals("postDate")){
            hql = "from Item where active is true and groupType = '"+groupType+"' "+searchPrice+searchCat+searchSubCat+" order by postDate desc";
        } else{
            hql = "from Item where active is true and groupType = '"+groupType+"' "+searchPrice+searchCat+searchSubCat+" order by "+sortBy;
        }
        Query query = session.createQuery(hql);
        List<Item> items = query.list();
        session.close();
        return items;
    }

    @Override
    public Item findById(Long id) {
        Session session = sessionFactory.openSession();
        Item item = session.get(Item.class,id);
        session.close();
        return item;
    }

    @Override
    public void save(Item item) {
        item.setImage(Utils.sizeImage(item.getImage()));
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(item);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void delete(Item item) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(item);
        session.getTransaction().commit();
        session.close();
    }
}
