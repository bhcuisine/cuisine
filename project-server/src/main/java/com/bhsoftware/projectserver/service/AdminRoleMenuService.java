package com.bhsoftware.projectserver.service;

import com.bhsoftware.projectserver.dao.AdminRoleMenuDao;
import com.bhsoftware.projectserver.entity.AdminRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
@Service
public class AdminRoleMenuService {

    @Autowired
    private AdminRoleMenuDao adminRoleMenuDao;

    public List<AdminRoleMenu> findAllByRid(int rid) {
        return adminRoleMenuDao.findAllByRid(rid);
    }

    public List<AdminRoleMenu> findAllByRidIn(List<Integer> rids) {
        return adminRoleMenuDao.findAllByRidIn(rids);
    }
}
