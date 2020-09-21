package com.bh.bhcuisine.service;

import com.bh.bhcuisine.dao.CastDao;
import com.bh.bhcuisine.entity.Cast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 成本盈利服务层
 */
@Service
public class CastService {
    /**
     * 注入成本实体类
     */
    @Autowired
    private CastDao castDao;

    /**
     * 返回分页list
     * @param rentTime
     * @param branchName
     * @return
     */
    public Page<Cast> findAllByRAndBranchNameAndRenTime(String rentTime, String branchName, String username, Pageable pageable){
        return castDao.findAllByRAndBranchNameAndRenTime(rentTime,branchName,username,pageable);
    }

    /**
     * 返回分页list
     * @param rentTime
     * @param branchName
     * @return
     */
    public Page<Cast> findAllByRAndBranchNameAndRenTime2(String rentTime, String branchName, Pageable pageable){
        return castDao.findAllByRAndBranchNameAndRenTime2(rentTime,branchName,pageable);
    }
    /**
     * 批量保存绩效率
     * @param performance 绩效率
     * @param
     */
    public void updatePerformanceByBranchNameIn(Integer performance,Integer id){
        castDao.updatePerformanceByBranchNameIn(performance,id);
    }

    /**
     * 添加数据
     * @param cast 盈利实体类
     */
    public void addCast(Cast cast){
        castDao.save(cast);
    }

    /**
     * 根据店名找到盈利数据表
     */
    public Cast findAllByBranchName(String branchName){
        return castDao.findAllByBranchName(branchName);
    }


    /**
     * 根据id得到实体类所有
     * @param id
     * @return
     */
    public Cast findAllById(Integer id){
        return castDao.findAllById(id);
    }

    /**
     * 根据时间查找盈利实体
     * @param rentTime
     * @return
     */
    public Cast findAllByRentTime(String rentTime){
        return castDao.findAllByRentTime(rentTime);
    }

}
