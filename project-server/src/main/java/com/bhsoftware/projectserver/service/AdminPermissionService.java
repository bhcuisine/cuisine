package com.bhsoftware.projectserver.service;

import com.bhsoftware.projectserver.dao.AdminPermissionDao;
import com.bhsoftware.projectserver.dao.AdminRolePermissionDao;
import com.bhsoftware.projectserver.entity.AdminPermission;
import com.bhsoftware.projectserver.entity.AdminRole;
import com.bhsoftware.projectserver.entity.AdminRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/14
 */
@Service
public class AdminPermissionService {

    @Autowired
    private AdminPermissionDao adminPermissionDao;

    @Autowired
    private AdminRolePermissionService adminRolePermissionService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminRolePermissionDao adminRolePermissionDao;

    public List<AdminPermission> list(){
        return adminPermissionDao.findAll();
    }

    public List<AdminPermission> listPermsByRoleId(int rid){
        List<Integer> pids = adminRolePermissionService.findAllByRid(rid)
                .stream()
                .map(AdminRolePermission::getPid)
                .collect(Collectors.toList());
        return adminPermissionDao.findAllById(pids);
    }


    public Set<String> listPermissionURLsByUser(String username){
        List<Integer> rids = adminRoleService.listRolesByUser(username)
                .stream()
                .map(AdminRole::getId)
                .collect(Collectors.toList());

        List<Integer> pids = adminRolePermissionDao.findAllByRidIn(rids)
                .stream()
                .map(AdminRolePermission::getPid)
                .collect(Collectors.toList());

        List<AdminPermission> perms = adminPermissionDao.findAllById(pids);

        Set<String> URLs = perms.stream()
                .map(AdminPermission::getUrl)
                .collect(Collectors.toSet());
        return URLs;
    }


}
