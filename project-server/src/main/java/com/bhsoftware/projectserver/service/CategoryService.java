package com.bhsoftware.projectserver.service;

import com.bhsoftware.projectserver.dao.CategoryDao;
import com.bhsoftware.projectserver.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/5
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public List<Category> list(){
        //Sort sort = new Sort(Sort.Direction.DESC,"id");
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        return categoryDao.findAll(sort);
    }

    public Category get(int id){

        Category category = categoryDao.findById(id).orElse(null);

        return category;
    }
}
