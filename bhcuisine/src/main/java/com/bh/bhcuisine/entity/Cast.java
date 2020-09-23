package com.bh.bhcuisine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * 成本盈利表实体类
 */
@Data
@Entity
@Table(name = "tb_cast")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Cast {

    /**
     * 成本表主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    /**
     * 店名
     */
    @Column(name = "branch_name")
    @JsonProperty("branchName")
    private String branchName;
    /**
     * 总营业额
     */
    @Column(name = "month_total")
    @JsonProperty("monthTotal")
    private Integer monthTotal;


    /**
     * 人工费用
     */
    @Column(name = "employee_total")
    @JsonProperty("employeeTotal")
    private Integer employeeTotal;


    /**
     * 房租
     */
    @Column(name = "rent_total")
    @JsonProperty("rentTotal")
    private Integer rentTotal;

    /**
     * 材料成本
     */
    @Column(name = "cost_total")
    @JsonProperty("costTotal")
    private Double costTotal;

    /**
     * 利润值
     */
    @Column(name = "profit_total")
    @JsonProperty("profitTotal")
    private Double profitTotal;


    /**
     * 绩效值
     */
    @Column(name = "performance_total")
    @JsonProperty("performanceTotal")
    private Double performanceTotal;

    /**
     * 绩效率
     */
    private Integer performance;

    /**
     * 费用时间
     */
    @Column(name = "rent_time")
    @JsonProperty("rentTime")
    private String rentTime;

}
