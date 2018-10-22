package net.myrents.service;

import net.myrents.dao.CategoryDao;
import net.myrents.dao.SubCategoryDao;
import net.myrents.model.Category;
import net.myrents.model.SubCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    @Autowired
    private SubCategoryDao subCategoryDao;

    @Override
    public List<SubCategory> findAll(Long id) {
        return subCategoryDao.findAll(id);
    }

    @Override
    public SubCategory findById(Long id) {
        return subCategoryDao.findById(id);
    }

    @Override
    public void save(SubCategory subCategory) {
        subCategoryDao.save(subCategory);
    }

    @Override
    public void delete(SubCategory subCategory) {
        subCategoryDao.delete(subCategory);
    }
}
