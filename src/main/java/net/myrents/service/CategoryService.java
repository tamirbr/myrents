package net.myrents.service;

import net.myrents.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> findAll(String type);
    List<String> findAllGroupTypes();
    Category findById(Long id);
    void save(Category category);
    void delete(Category category);
}
