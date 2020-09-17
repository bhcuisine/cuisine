package com.bh.bhcuisine.dao;

import com.bh.bhcuisine.entity.Cast;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CastDao extends JpaRepository<Cast,Integer>, JpaSpecificationExecutor<Cast> {

    /**
     * 按月份查询营业额，人工，房租
     * @param rentTime
     * @param branchName
     * @return
     */
    @Query(value = "SELECT u.id,c.id AS cid,c.cost_total,c.profit_total,c.performance_total,c.employee_total,u.username,c.month_total,c.rent_time,c.rent_total," +
            "u.performance,u.branch_name,u.branch_location " +
            "FROM tb_cast c LEFT JOIN tb_user u " +
            "ON c.branch_name=u.branch_name WHERE if(?1 !='',c.rent_time=?1,1=1) " +
            "AND if(?2 !='',c.branch_name=?2,1=1) AND if(?3 !='',u.username=?3,1=1) ",nativeQuery = true)
    Page<Cast> findAllByRAndBranchNameAndRenTime(@Param(value = "rentTime")String rentTime, @Param(value = "branchName")String branchName, @Param(value = "username")String username, Pageable pageable);
}
