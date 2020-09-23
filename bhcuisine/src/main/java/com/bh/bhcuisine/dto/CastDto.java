package com.bh.bhcuisine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class CastDto {

    /**
     * 成本表主键id
     */
    private Integer id;
    /**
     * 店名
     */

    private String branchName;
    /**
     * 总营业额
     */

    private Integer monthTotal;


    /**
     * 人工费用
     */

    private Integer employeeTotal;


    /**
     * 房租
     */

    private Integer rentTotal;

    /**
     * 材料成本
     */

    private Double costTotal;

    /**
     * 利润值
     */

    private Double profitTotal;


    /**
     * 绩效值
     */

    private Double performanceTotal;

    /**
     * 绩效率
     */
    private Integer performance;

    /**
     * 费用时间
     */

    private String rentTime;



    private String addTime;

    private Integer uid;

}
