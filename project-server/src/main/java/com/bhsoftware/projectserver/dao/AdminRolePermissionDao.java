package com.bhsoftware.projectserver.dao;

import com.bhsoftware.projectserver.entity.AdminRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/14
 */
public interface AdminRolePermissionDao extends JpaRepository<AdminRolePermission, Integer> {

    List<AdminRolePermission> findAllByRid(int rid);

    List<AdminRolePermission> findAllByRidIn(List<Integer> rids);
}
