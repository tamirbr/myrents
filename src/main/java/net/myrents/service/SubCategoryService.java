package net.myrents.service;

import net.myrents.model.SubCategory;

import java.util.List;

public interface SubCategoryService {
    List<SubCategory> findAll(Long id);
    SubCategory findById(Long id);
    void save(SubCategory subCategory);
    void delete(SubCategory subCategory);
}
