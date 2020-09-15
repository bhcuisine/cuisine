package com.bhsoftware.projectserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
@Entity
@Data
@Table(name = "admin_user_role")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class AdminUserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int uid;   //用户id

    private int rid;   //角色id
}
