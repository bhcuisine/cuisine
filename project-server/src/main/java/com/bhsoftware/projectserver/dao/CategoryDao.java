package com.bhsoftware.projectserver.dao;

import com.bhsoftware.projectserver.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/5
 */
public interface CategoryDao extends JpaRepository<Category,Integer> {

}
