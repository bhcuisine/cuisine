package com.bh.bhcuisine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 数据实体类
 */
@Data
@Entity
@Table(name = "tb_materials")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Materials implements Serializable {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    /**
     * 用户门店id
     */
    private Integer uid;
    /**
     * 材料名称
     */
    @Column(name = "materials_name")
    @JsonProperty("materialsName")
    private String materialsName;
    /**
     * 种类id
     */
    @Column(name = "category_id")
    @JsonProperty("categoryId")
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
     * 每个材料成本materials_total
     */
    @Column(name = "materials_total")
    @JsonProperty("materialsTotal")
    private Double materialsTotal;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @JsonProperty("updateTime")
    private Date updateTime;

    /**
     * 添加时间
     */
    @Column(name = "add_time")
    @JsonProperty("addTime")
    private String addTime;

    /**
     * 状态
     */
    private Integer status;

}
