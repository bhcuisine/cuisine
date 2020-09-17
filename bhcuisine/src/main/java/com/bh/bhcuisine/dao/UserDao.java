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
    @Query("update User u set u.password =?1 ")
    void updatePassWord(@Param(value = "password")String password);

    /**
     * 批量修改绩效
     * @param performance 绩效
     * @param id id
     */
    @Transactional
    @Modifying
    @Query("update User u set u.performance =:performance where u.id =:id")
    void updatePerformanceByIdIn(@Param(value = "performance")Integer performance,@Param(value = "id")Integer id);

    /**
     * 获取绩效
     * @param username
     * @return
     */
    @Query(value = "SELECT u.performance,u.id,u.username,u.branch_name,u.branch_location FROM tb_user u WHERE u.username=:username",nativeQuery = true)
    Integer getPerformance(@Param(value = "username")String username);
}
