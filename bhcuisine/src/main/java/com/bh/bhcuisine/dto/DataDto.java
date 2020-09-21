package com.bh.bhcuisine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
public class DataDto {
    /**
     * 用户门店id
     */
    private Integer uid;
    /**
     * 材料名称
     */
    private String materialsName;
    /**
     * 种类id
     */
    private Integer categoryId;

    /**
     * 价格
     */
    private Double price;
    /**
     * 数量
     */
    private Integer quanty;
    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 添加时间
     */
    private String addTime;
}
