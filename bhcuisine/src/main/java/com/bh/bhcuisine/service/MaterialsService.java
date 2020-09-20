package com.bh.bhcuisine.service;

import com.bh.bhcuisine.dao.MaterialsDao;
import com.bh.bhcuisine.entity.Materials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 材料数据实体service层
 */
@Service
public class MaterialsService {

    /**
     * 注入材料数据dao层
     */
    @Autowired
    private MaterialsDao materialsDao;

    /**
     * f返回分页查询对象
     * @param username 用户名
     * @param branchName
     * @param addTime
     * @param pageable
     * @return
     */
//    public Page<Materials> getAllByUserNameAndBranchNameAndAddTime(String username, String branchName, String addTime, Pageable pageable){
//        return materialsDao.getAllByUserNameAndBranchNameAndAddTime(username,branchName,addTime,pageable);
//    }

    /**
     * 材料
     * @param materials
     */
    public void addMaterials(Materials materials){
        materialsDao.save(materials);
    }

}
