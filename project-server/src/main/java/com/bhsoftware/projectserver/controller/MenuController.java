package com.bhsoftware.projectserver.controller;

import com.bhsoftware.projectserver.entity.AdminMenu;
import com.bhsoftware.projectserver.result.Result;
import com.bhsoftware.projectserver.result.ResultFactory;
import com.bhsoftware.projectserver.service.AdminMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
@RestController
public class MenuController {

    @Autowired
    private AdminMenuService adminMenuService;

    @GetMapping("/api/menu")
    public Result menu(){
        return ResultFactory.buildSuccessResult(adminMenuService.getMenusByCurrentUser());
    }

//    @GetMapping("/api/menu")
//     public List<AdminMenu> menu(){
//         return adminMenuService.getMenusByCurrentUser();
//     }

    @GetMapping("/api/admin/role/menu")
    public Result listAllMenus(){
        return ResultFactory.buildSuccessResult(adminMenuService.getMenusByRoleId(1));
    }
}
