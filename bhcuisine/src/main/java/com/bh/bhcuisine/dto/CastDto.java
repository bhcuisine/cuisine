package com.bh.bhcuisine.dto;

import lombok.Data;

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

    private Double  monthTotal;


    /**
     * 人工费用
     */

    private Double  employeeTotal;


    /**
     * 房租
     */

    private Double rentTotal;

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
