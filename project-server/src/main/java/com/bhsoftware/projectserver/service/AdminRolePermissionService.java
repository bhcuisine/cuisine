package com.bhsoftware.projectserver.service;

import com.bhsoftware.projectserver.dao.AdminRolePermissionDao;
import com.bhsoftware.projectserver.entity.AdminRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/14
 */
@Service
public class AdminRolePermissionService {

    @Autowired
    AdminRolePermissionDao adminRolePermissionDao;

    List<AdminRolePermission> findAllByRid(int rid) {
        return adminRolePermissionDao.findAllByRid(rid);
    }
}
