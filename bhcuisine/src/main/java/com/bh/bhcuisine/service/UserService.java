package com.bh.bhcuisine.service;

import com.bh.bhcuisine.dao.UserDao;
import com.bh.bhcuisine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public void updatePassWord(String password){
        userDao.updatePassWord(password);
    }

    /**
     * 批量保存绩效
     * @param performance
     * @param ids
     */
    public void updatePerformance(Integer performance,Integer id){
        userDao.updatePerformanceByIdIn(performance,id);
    }

    /**
     * 实现添加新店
     * @param u 用户实体
     */
    public void addUser(User u){
        userDao.save(u);
    }

    /**
     * 得到绩效
     * @param username
     * @return
     */
    public Integer getPerformance(String username){
        return userDao.getPerformance(username);
    }

}
