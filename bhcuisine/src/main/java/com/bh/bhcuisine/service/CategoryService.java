package com.bh.bhcuisine.service;

import com.bh.bhcuisine.dao.CategoryDao;
import com.bh.bhcuisine.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    /**
     * 分类
     * @param category
     */
    public void addCategory(Category category) {
        categoryDao.save(category);
    }

    public List<Category> fallAll(){
        return categoryDao.findAll();
    }

}
