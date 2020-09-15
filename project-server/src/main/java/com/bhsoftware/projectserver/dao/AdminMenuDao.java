package com.bhsoftware.projectserver.dao;

import com.bhsoftware.projectserver.entity.AdminMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
public interface AdminMenuDao extends JpaRepository<AdminMenu,Integer> {

    AdminMenu findById(int id);

    List<AdminMenu> findAllByParentId(int parentId);
}
