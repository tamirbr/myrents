package net.myrents.dao;

import net.myrents.model.Category;
import net.myrents.model.SubCategory;

import java.util.List;

public interface SubCategoryDao {
    List<SubCategory> findAll(Long id);
    SubCategory findById(Long id);
    void save(SubCategory subCategory);
    void delete(SubCategory subCategory);
}
