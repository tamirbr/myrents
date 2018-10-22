package net.myrents.service;

import net.myrents.dao.CategoryDao;
import net.myrents.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> findAll(String type) {
        return categoryDao.findAll(type);
    }

    @Override
    public List<String> findAllGroupTypes() {
        return categoryDao.findAllGroupTypes();
    }

    @Override
    public Category findById(Long id) {
        return categoryDao.findById(id);
    }

    @Override
    public void save(Category category) {
        categoryDao.save(category);
    }

    @Override
    public void delete(Category category) {
        categoryDao.delete(category);
    }
}
