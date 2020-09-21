package com.bh.bhcuisine.dao;

import com.bh.bhcuisine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
/**
 * 用户dao层
 */
@Repository
public interface UserDao extends JpaRepository<User,Integer>  , JpaSpecificationExecutor<User> {
    /**
     * 根据用户名得到用户实体类
     * @param username
     * @return
     */
    User getByUsername(String username);

    /**
     * 修改密码
     * @param password
     */
    @Transactional
    @Modifying
    @Query("update User u set u.password =?1 where u.id=?2")
    void updatePassWord(@Param(value = "password")String password,@Param(value = "id")Integer id);


}
