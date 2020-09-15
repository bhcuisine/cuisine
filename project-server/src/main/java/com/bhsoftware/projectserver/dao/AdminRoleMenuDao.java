package com.bhsoftware.projectserver.dao;

import com.bhsoftware.projectserver.entity.AdminRoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
public interface AdminRoleMenuDao extends JpaRepository<AdminRoleMenu,Integer> {

    List<AdminRoleMenu> findAllByRid(int rid);

    List<AdminRoleMenu> findAllByRidIn(List<Integer> rids);

    void deleteAllByRid(int rid);
}
