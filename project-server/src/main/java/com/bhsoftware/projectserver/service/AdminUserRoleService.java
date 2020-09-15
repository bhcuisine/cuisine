package com.bhsoftware.projectserver.service;

import com.bhsoftware.projectserver.dao.AdminUserRoleDao;
import com.bhsoftware.projectserver.entity.AdminUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
@Service
public class AdminUserRoleService {

    @Autowired
    private AdminUserRoleDao adminUserRoleDao;

    public List<AdminUserRole> listAllByUid(int uid){
        return adminUserRoleDao.findAllByUid(uid);
    }
}
