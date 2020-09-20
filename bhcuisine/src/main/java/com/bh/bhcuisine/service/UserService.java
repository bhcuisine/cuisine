package com.bh.bhcuisine.service;

import com.bh.bhcuisine.dao.UserDao;
import com.bh.bhcuisine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 用户服务层
 */
@Service
public class UserService {

    /**
     * 注入userDao
     */
    @Autowired
    private UserDao userDao;

    /**
     * 根据用户名得到用户实体
     * @param username
     * @return
     */
    public User getByUsername(String username){
        return userDao.getByUsername(username);
    }

    /**
     * 修改用户密码
     * @param password
     */
    public void updatePassWord(String password,Integer id){
        userDao.updatePassWord(password,id);
    }



    /**
     * 实现添加新店
     * @param u 用户实体
     */
    public void addUser(User u){
        userDao.save(u);
    }

    /**
     * 得到所有店名
     */
    public List<User> getAllBranchName(){
        return userDao.findAll();
    }


}
