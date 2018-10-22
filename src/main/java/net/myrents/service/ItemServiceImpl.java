package net.myrents.service;

import net.myrents.dao.ItemDao;
import net.myrents.model.Item;
import net.myrents.model.User;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;

    @Override
    public List<Item> findAll(String groupType) {
        return itemDao.findAll(groupType);
    }

    @Override
    public List<Item> searchAll(String sortBy,String priceStart, String priceEnd, String price,String type, Long cat, Long subCat) {
        return itemDao.searchAll(sortBy,priceStart,priceEnd,price,type,cat,subCat);
    }

    @Override
    public Item findById(Long id) {
        return itemDao.findById(id);
    }

    @Override
    public void save(Item item) {
        itemDao.save(item);
    }

    @Override
    public void delete(Item item) {
        itemDao.delete(item);
    }
}
