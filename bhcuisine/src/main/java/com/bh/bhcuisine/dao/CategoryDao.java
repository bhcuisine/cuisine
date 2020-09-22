package com.bh.bhcuisine.dao;

import com.bh.bhcuisine.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 分类dao层
 */
@Repository
public interface CategoryDao extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {

    Category findAllByCategoryName(String StringName);
}
