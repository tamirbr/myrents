package net.myrents.dao;

import net.myrents.model.Item;

import java.util.List;

public interface ItemDao {
    List<Item> findAll(String groupType);
    List<Item> searchAll(String sortBy,String priceStart, String priceEnd, String price,String type, Long cat, Long subCat);
    Item findById(Long id);
    void save(Item item);
    void delete(Item item);
}
