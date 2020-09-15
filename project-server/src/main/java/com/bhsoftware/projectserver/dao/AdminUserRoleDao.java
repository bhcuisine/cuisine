package com.bhsoftware.projectserver.dao;

import com.bhsoftware.projectserver.entity.AdminUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
public interface AdminUserRoleDao extends JpaRepository<AdminUserRole,Integer> {

    List<AdminUserRole> findAllByUid(int uid);

    void deleteAllByUid(int uid);
}
