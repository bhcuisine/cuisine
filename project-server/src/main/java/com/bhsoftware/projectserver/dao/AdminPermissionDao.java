package com.bhsoftware.projectserver.dao;

import com.bhsoftware.projectserver.entity.AdminPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/14
 */
public interface AdminPermissionDao extends JpaRepository<AdminPermission,Integer> {

    AdminPermission findById(int id);
}
