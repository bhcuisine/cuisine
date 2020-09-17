package com.bh.bhcuisine.dao;

import com.bh.bhcuisine.entity.Cast;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.sql.Time;
import java.util.List;
@Repository
public interface CastDao extends JpaRepository<Cast,Integer>, JpaSpecificationExecutor<Cast> {

    /**
     * 按月份查询营业额，人工，房租
     * @param rentTime
     * @param branchName
     * @return
     */
    @Query(value = "SELECT u.id AS userId,c.id ,c.cost_total,c.profit_total,c.performance_total,c.employee_total,u.username,c.month_total,c.rent_time,c.rent_total," +
            "c.performance,u.branch_name,u.branch_location " +
            "FROM tb_cast c LEFT JOIN tb_user u " +
            "ON c.branch_name=u.branch_name WHERE if(?1 !='',c.rent_time=?1,1=1) " +
            "AND if(?2 !='',c.branch_name=?2,1=1) AND if(?3 !='',u.username=?3,1=1) ",nativeQuery = true)
    Page<Cast> findAllByRAndBranchNameAndRenTime(@Param(value = "rentTime")String rentTime, @Param(value = "branchName")String branchName, @Param(value = "username")String username, Pageable pageable);

    /**
     * 批量修改绩效率
     * @param performance 绩效率
     * @param id id
     */
    @Transactional
    @Modifying
    @Query("update Cast c set c.performance =:performance where c.id=:id")
    void updatePerformanceByBranchNameIn(@Param(value = "performance")Integer performance,Integer id);


     Cast findAllById(Integer id);

    Cast findAllByBranchName(String branchName);

    /**
     * 插入已存在数据
     * @param branchName
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Cast c set c.costTotal =:costTotal,c.employeeTotal=:employeeTotal,c.monthTotal=:monthTotal,c.rentTotal=:rentTotal,c.profitTotal=:profitTotal,c.performanceTotal=:performanceTotal where c.id=:id")
    void updateCast(@Param(value = "costTotal") Double costTotal,
                    @Param(value = "employeeTotal") Integer employeeTotal,
                    @Param(value = "monthTotal")Integer monthTotal,
                    @Param(value = "rentTotal")Integer rentTotal,
                    @Param(value = "profitTotal") Double profitTotal,
                    @Param(value = "performanceTotal")Double performanceTotal,
                    @Param(value = "id") Integer id);
}
