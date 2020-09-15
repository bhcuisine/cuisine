package com.bhsoftware.projectserver.controller;

import com.bhsoftware.projectserver.result.Result;
import com.bhsoftware.projectserver.result.ResultFactory;
import com.bhsoftware.projectserver.service.AdminPermissionService;
import com.bhsoftware.projectserver.service.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/14
 */
@RestController
public class RoleController {

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminPermissionService adminPermissionService;

    @GetMapping("/api/admin/role/perm")
    public Result listPerms(){
        return ResultFactory.buildSuccessResult(adminPermissionService.list());
    }

    @GetMapping("/api/admin/role")
    public Result listRoles(){
        return ResultFactory.buildSuccessResult(adminRoleService.listWithPermsAndMenus());
    }


}
