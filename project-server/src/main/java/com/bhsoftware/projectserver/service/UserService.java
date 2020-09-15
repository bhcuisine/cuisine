package com.bhsoftware.projectserver.service;

import com.bhsoftware.projectserver.dao.UserDao;
import com.bhsoftware.projectserver.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/2
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public boolean isExist(String username){
        User user = getByName(username);

        return null != user;
    }

    public User findByUsername(String username){
        return userDao.findByUsername(username);
    }

    public User getByName(String username) {
        return userDao.findByUsername(username);
    }

    public User get(String username,String password){
        return userDao.getByUsernameAndPassword(username,password);
    }

    public void add(User user){
        userDao.save(user);
    }
}
