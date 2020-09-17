package com.bh.bhcuisine.service;

import com.bh.bhcuisine.dao.CategoryDao;
import com.bh.bhcuisine.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    public void addCategory(Category category) {
        categoryDao.save(category);
    }

}
