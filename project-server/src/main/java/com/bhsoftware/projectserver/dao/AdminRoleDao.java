package com.bhsoftware.projectserver.dao;

import com.bhsoftware.projectserver.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/14
 */
public interface AdminRoleDao extends JpaRepository<AdminRole,Integer> {

    AdminRole findById(int id);
}
