package com.bh.bhcuisine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
@Entity
@Table(name = "tb_user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class User implements Serializable {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 使能状态
     */
    private Integer enabled;
    /**
     * 店名
     */
    @Column(name = "branch_name")
    @JsonProperty("branchName")
    private String branchName;
    /**
     * 店地理位置
     */
    @Column(name = "branch_location")
    @JsonProperty("branchLocation")
    private String branchLocation;
    /**
     * 身份标识：1-BOSS;2-店主
     */
    private Integer status;



}
