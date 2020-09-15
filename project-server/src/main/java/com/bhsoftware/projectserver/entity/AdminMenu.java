package com.bhsoftware.projectserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/11
 */
@Data
@Entity
@Table(name = "admin_menu")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class AdminMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;           //唯一标识

    private String path;      //与vue路由中的name属性对应

    private String nameZh;    //中文名称，用于渲染导航栏界面

    private String iconCls;   //element图标类名，用于渲染菜单名称前的小图标

    private String component; //组件名，用于解析路由对应的组件

    private int parentId;     //父节点(id),用于存储导航栏层级关系

    @Transient
    private List<AdminMenu> children;    //存储子结点
}
