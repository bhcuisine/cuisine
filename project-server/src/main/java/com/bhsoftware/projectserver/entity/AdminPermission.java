package com.bhsoftware.projectserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
@Data
@Entity
@Table(name = "admin_permission")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class AdminPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;    //权限名称

    private String desc_;   //权限描述

    private String url;     //权限url
}
