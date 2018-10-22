package net.myrents.service;

import net.myrents.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> findAll(String groupType);
    List<Item> searchAll(String sortBy,String priceStart, String priceEnd, String price,String type, Long cat, Long subCat);
    Item findById(Long id);
    void save(Item item);
    void delete(Item item);
}
