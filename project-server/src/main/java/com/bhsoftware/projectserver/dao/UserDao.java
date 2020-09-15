package com.bhsoftware.projectserver.dao;

import com.bhsoftware.projectserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/2
 */
public interface UserDao extends JpaRepository<User,Integer> {

    User findByUsername(String username);

    User getByUsernameAndPassword(String username,String password);
}
