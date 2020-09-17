package com.bh.bhcuisine.dao;


import com.bh.bhcuisine.entity.Materials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 数据dao层
 */
@Repository
public interface MaterialsDao extends JpaRepository<Materials,Integer>, JpaSpecificationExecutor<Materials> {

    /**
     * 调用本地查询 实现店名，添加时间搜索
     * @param username 用户名
     * @param branchName 店名
     * @param addTime 添加时间
     * @param pageable 分页
     * @return
     */
    @Query(value = "select tb_user.id AS userid,tb_user.username," +
            "tb_user.password,tb_user.salt," +
            "tb_user.enabled,tb_user.branch_name,tb_user.branch_location,tb_user.performance," +
            "tb_category_name.id AS categoryid,tb_user.status,tb_materials.category_id," +
            "tb_materials.id,tb_materials.uid,tb_materials.materials_name,tb_materials.price,tb_materials.quanty,tb_materials.update_time,tb_materials.add_time" +
            " FROM tb_materials LEFT JOIN tb_user " +
            "ON tb_user.id=tb_materials.uid LEFT JOIN tb_category_name " +
            "ON tb_materials.category_id=tb_category_name.id WHERE " +
            "if(?1 !='',username=?1,1=1) AND if(?2 !='',tb_user.branch_name=?2,1=1) " +
            "AND if(?3 !='',tb_materials.add_time=?3,1=1) ",nativeQuery = true)
    Page<Materials> getAllByUserNameAndBranchNameAndAddTime(String username, String branchName, String addTime,Pageable pageable);
}
