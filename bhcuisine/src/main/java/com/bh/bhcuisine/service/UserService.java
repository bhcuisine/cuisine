package com.bh.bhcuisine.service;

import com.bh.bhcuisine.dao.UserDao;
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
}
