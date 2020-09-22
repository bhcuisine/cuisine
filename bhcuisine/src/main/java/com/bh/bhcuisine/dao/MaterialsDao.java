package com.bh.bhcuisine.dao;


import com.bh.bhcuisine.entity.Materials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;
/**
 * 数据dao层
 */
@Repository
public interface MaterialsDao extends JpaRepository<Materials, Integer>, JpaSpecificationExecutor<Materials> {

    /**
     * 调用本地查询 实现店名，添加时间搜索
     *
     * @param username   用户名
     * @param branchName 店名
     * @param addTime    添加时间
     * @param pageable   分页
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
            "AND if(?3 !='',tb_materials.add_time=?3,1=1) ", nativeQuery = true)
    Page<Materials> getAllByUserNameAndBranchNameAndAddTime(String username, String branchName, String addTime, Pageable pageable);


    List<Materials> findAllByUidAndAddTime(@Param(value = "uid") Integer uid,@Param(value = "addTime")String addTime);

    List<Materials> findAllByUidIn(@Param(value = "uid") Integer uid);


    @Query(value = "select * from tb_materials where if(?1 !='',tb_materials.id=?1,1=1)",nativeQuery = true)
    Page<Materials> getAll(@Param(value = "id") Integer id,Pageable pageable);
    /**
     * 插入已存在数据
     *
     * @param branchName
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Materials m set m.materialsName=:materialsName,m.updateTime=:updateTime,m.materialsTotal=:materialsTotal,m.price=:price,m.quanty=:quanty where m.id=:id")
    void updateMaterials(@Param(value = "materialsName") String materialsName,
                         @Param(value = "updateTime") Date updateTime,
                         @Param(value = "materialsTotal") Double materialsTotal,
                         @Param(value = "price") Double price,
                         @Param(value = "quanty") Integer quanty,
                         @Param(value = "id") Integer id);


    /**
     * 根据id删除材料
     * @param id
     */
    void deleteById(@Param(value = "id") Integer id);
}
