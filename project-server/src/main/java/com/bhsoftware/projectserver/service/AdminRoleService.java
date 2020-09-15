package com.bhsoftware.projectserver.service;

import com.bhsoftware.projectserver.dao.AdminRoleDao;
import com.bhsoftware.projectserver.entity.AdminMenu;
import com.bhsoftware.projectserver.entity.AdminPermission;
import com.bhsoftware.projectserver.entity.AdminRole;
import com.bhsoftware.projectserver.entity.AdminUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/14
 */
@Service
public class AdminRoleService {

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Autowired
    private AdminPermissionService adminPermissionService;

    @Autowired
    private AdminMenuService adminMenuService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminUserRoleService adminUserRoleService;

    public List<AdminRole> listWithPermsAndMenus(){
        List<AdminRole> roles = adminRoleDao.findAll();
        List<AdminPermission> perms;
        List<AdminMenu> menus;
        for (AdminRole role : roles) {
            perms = adminPermissionService.listPermsByRoleId(role.getId());
            menus = adminMenuService.getMenusByRoleId(role.getId());
            role.setPerms(perms);
            role.setMenus(menus);
        }
        return roles;
    }

    public List<AdminRole> findAll(){
        return adminRoleDao.findAll();
    }

    public List<AdminRole> listRolesByUser(String username){
        int uid = userService.findByUsername(username).getId();
        List<Integer> rids = adminUserRoleService.listAllByUid(uid)
                .stream()
                .map(AdminUserRole::getRid)
                .collect(Collectors.toList());
        return adminRoleDao.findAllById(rids);
    }
}
