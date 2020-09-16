package com.bh.bhcuisine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 种类表实体类
 */
@Data
@Entity
@Table(name = "tb_category_name")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class Category implements Serializable {

    /**
     * 种类主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    /**
     * 种类名称
     */
    @Column(name = "category_name")
    @JsonProperty("categoryName")
    private String categoryName;
}
