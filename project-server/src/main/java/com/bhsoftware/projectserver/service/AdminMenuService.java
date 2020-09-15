package com.bhsoftware.projectserver.service;

import com.bhsoftware.projectserver.dao.AdminMenuDao;
import com.bhsoftware.projectserver.dao.AdminUserRoleDao;
import com.bhsoftware.projectserver.entity.AdminMenu;
import com.bhsoftware.projectserver.entity.AdminRoleMenu;
import com.bhsoftware.projectserver.entity.AdminUserRole;
import com.bhsoftware.projectserver.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
@Service
public class AdminMenuService {

    @Autowired
    private AdminMenuDao adminMenuDao;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminUserRoleService adminUserRoleService;

    @Autowired
    private AdminRoleMenuService adminRoleMenuService;


    //获取菜单父结点
    public List<AdminMenu> getAllByParentId(int parentId) {
        return adminMenuDao.findAllByParentId(parentId);
    }

    //获取当前用户菜单
    public List<AdminMenu> getMenusByCurrentUser() {

        //获取当前用户
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.findByUsername(username);

        /**
         * 获取当前用户所有角色id
         *        stream 简化列表处理
         *        map() 提取集合中的某一属性
         */
        List<Integer> rids = adminUserRoleService.listAllByUid(user.getId())
                .stream()
                .map(AdminUserRole::getRid)
                .collect(Collectors.toList());

        //获取菜单项的角色id
        List<Integer> menuIds = adminRoleMenuService.findAllByRidIn(rids)
                .stream()
                .map(AdminRoleMenu::getMid)
                .collect(Collectors.toList());

        // 获取菜单---distinct() 去重操作
        List<AdminMenu> menus = adminMenuDao.findAllById(menuIds)
                .stream()
                .distinct()
                .collect(Collectors.toList());

        handleMenus(menus);
        return menus;
    }

    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<Integer> menuIds = adminRoleMenuService.findAllByRid(rid)
                .stream()
                .map(AdminRoleMenu::getMid)
                .collect(Collectors.toList());
        List<AdminMenu> menus = adminMenuDao.findAllById(menuIds);

        handleMenus(menus);
        return menus;
    }

    /**
     * 生成菜单框架
     * 查询的菜单数据列表整合成具有层级关系的菜单树
     * jdk8以上版本：lambda表达式
     */
    public void handleMenus(List<AdminMenu> menus) {
        menus.forEach(menu -> {
            List<AdminMenu> children = getAllByParentId(menu.getId());
            menu.setChildren(children);
        });

        menus.removeIf(menu -> menu.getParentId() != 0);
    }

//    public void handleMenus(List<AdminMenu> menus){
//        for (AdminMenu menu: menus){
//            List<AdminMenu> children = getAllByParentId(menu.getId());
//            menu.setChildren(children);
//        }
//        Iterator<AdminMenu> iterator = menus.iterator();
//        while(iterator.hasNext()){
//            AdminMenu menu = iterator.next();
//            if (menu.getParentId()!=0){
//                iterator.remove();
//            }
//        }
//    }
}
