package net.myrents.dao;

import net.myrents.model.Category;
import net.myrents.model.Item;

import java.util.List;

public interface CategoryDao {
    List<Category> findAll(String type);
    List<String> findAllGroupTypes();
    Category findById(Long id);
    void save(Category category);
    void delete(Category category);
}
