package com.bh.bhcuisine.service;

import com.bh.bhcuisine.dao.CastDao;
import com.bh.bhcuisine.entity.Cast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import java.util.List;
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
}
