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

    /**
     * 根据种类名称得到种类实体
     * @param StringName
     * @return
     */
    Category findAllByCategoryName(String StringName);
}
