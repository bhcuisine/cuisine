package com.bh.bhcuisine.dao;

import com.bh.bhcuisine.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/**
 * 用户dao层
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    /**
     * 根据用户名得到用户实体类
     *
     * @param username
     * @return
     */
    User getByUsername(String username);

    /**
     * 根据店名查找实体类
     * @return
     */
    User findAllByBranchName(String branchName);

    /**
     * 修改密码
     *
     * @param password
     */
    @Transactional
    @Modifying
    @Query("update User u set u.password =?1 where u.id=?2")
    void updatePassWord(@Param(value = "password") String password, @Param(value = "id") Integer id);


    /**
     * 根据id得到用户实体
     * @param id
     * @return
     */
    User findAllById(@Param(value = "id")Integer id);

    /**
     * 修改密码
     *
     * @param password
     */
    @Transactional
    @Modifying
    @Query("update User u set u.password =?1 where u.username=?2")
    void UpdateUserPassword(@Param(value = "password")String password,@Param(value = "username")String username);



    @Query(value = "select * from tb_user where tb_user.enabled=1 AND tb_user.username!='总裁'",nativeQuery = true)
    Page<User> findAllUserByEnabled(Pageable pageable);

}
