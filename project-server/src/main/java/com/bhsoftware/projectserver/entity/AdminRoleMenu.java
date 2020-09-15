package com.bhsoftware.projectserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
@Entity
@Data
@Table(name = "admin_role_menu")
@ToString
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class AdminRoleMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    private int rid;   //角色id

    private int mid;   //菜单id
}
