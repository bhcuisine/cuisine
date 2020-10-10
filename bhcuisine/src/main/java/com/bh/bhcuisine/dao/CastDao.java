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
public interface CastDao extends JpaRepository<Cast, Integer>, JpaSpecificationExecutor<Cast> {

    /**
     * 按月份查询营业额，人工，房租
     *
     * @return
     */
    @Query(value = "SELECT *" +
            "            FROM tb_cast LEFT JOIN tb_user" +
            "                  ON tb_cast.branch_name=tb_user.branch_name " +
            "where if(?1 is not null,tb_cast.rent_time=?1,1=1) " +
            "AND if(?2 is not null,tb_cast.branch_name=?2,1=1) AND if(?3 is not null,tb_user.username=?3,1=1) ORDER BY tb_cast.rent_time desc", nativeQuery = true)
    Page<Cast> findAllByRAndBranchNameAndRenTime(@Param(value = "rentTime") String rentTime, @Param(value = "branchName") String branchName, @Param(value = "username") String username, Pageable pageable);

    @Query(value = "SELECT *" +
            "            FROM tb_cast LEFT JOIN tb_user" +
            "                  ON tb_cast.branch_name=tb_user.branch_name " +
            "where if(?1 is not null,tb_cast.rent_time=?1,1=1) " +
            "AND if(?2 is not null,tb_cast.branch_name=?2,1=1) ORDER BY tb_cast.rent_time desc", nativeQuery = true)
    Page<Cast> findAllByRAndBranchNameAndRenTime2(@Param(value = "rentTime") String rentTime, @Param(value = "branchName") String branchName,Pageable pageable);

    /**
     * 批量修改绩效率
     *
     * @param performance 绩效率
     * @param id          id
     */
    @Transactional
    @Modifying
    @Query("update Cast c set c.performance =:performance where c.id=:id")
    void updatePerformanceByBranchNameIn(@Param(value = "performance") Integer performance, @Param(value = "id")Integer id);

    /**
     * 根据id获取盈利实体
     *
     * @param id
     * @return
     */
    Cast findAllById(Integer id);

    Cast findAllByBranchName(String branchName);

    @Query(value = "select * from tb_cast where tb_cast.rent_time=?1 AND tb_cast.branch_name=?2",nativeQuery = true)
    Cast getAllByRentTimeAndBranchName(@Param(value = "rentTime")String rentTime,@Param(value = "branchName") String branchName);

    /**
     * 批量查找绩效率
     *
     * @param id
     * @return
     */
    List<Cast> findAllByIdIn(List<Integer> id);


    /**
     * 根据时间找盈利实体
     *
     * @param rentTime
     * @return
     */
    Cast findAllByRentTime(@Param(value = "rentTime")String rentTime);
    /**
     * 插入已存在数据
     *
     * @param branchName
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Cast c set c.costTotal =:costTotal,c.employeeTotal=:employeeTotal,c.monthTotal=:monthTotal,c.rentTotal=:rentTotal,c.profitTotal=:profitTotal,c.performanceTotal=:performanceTotal where c.id=:id")
    void updateCast(@Param(value = "costTotal") Double costTotal,
                    @Param(value = "employeeTotal") Double employeeTotal,
                    @Param(value = "monthTotal") Double monthTotal,
                    @Param(value = "rentTotal") Double rentTotal,
                    @Param(value = "profitTotal") Double profitTotal,
                    @Param(value = "performanceTotal") Double performanceTotal,
                    @Param(value = "id") Integer id);

    /**
     * 更新数据
     * @param costTotal
     * @param employeeTotal
     * @param monthTotal
     * @param rentTotal
     * @param profitTotal
     * @param performanceTotal
     * @param rentTime
     */
    @Transactional
    @Modifying
    @Query("update Cast c set c.costTotal =:costTotal,c.employeeTotal=:employeeTotal,c.monthTotal=:monthTotal,c.rentTotal=:rentTotal,c.profitTotal=:profitTotal,c.performanceTotal=:performanceTotal where c.rentTime=:rentTime")
    void updateCast2(@Param(value = "costTotal") Double costTotal,
                    @Param(value = "employeeTotal") Double employeeTotal,
                    @Param(value = "monthTotal") Double monthTotal,
                    @Param(value = "rentTotal") Double rentTotal,
                    @Param(value = "profitTotal") Double profitTotal,
                    @Param(value = "performanceTotal") Double performanceTotal,
                    @Param(value = "rentTime") String rentTime
                    );

}
