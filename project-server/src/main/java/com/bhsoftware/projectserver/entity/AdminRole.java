package com.bhsoftware.projectserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin_role")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class AdminRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name; //角色名称

    private String nameZh;//角色中文名称

    private boolean enabled;//角色状态

    @Transient
    private List<AdminPermission> perms;

    @Transient
    private List<AdminMenu> menus;
}
