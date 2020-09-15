package com.bhsoftware.projectserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import javax.validation.constraints.NotEmpty;
import javax.persistence.*;



/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/7/31
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user")
@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;


    /**
     * Username不能为空
     */
   // @NotNull(message = "用户名不能为空")
    private String username;

    private String password;

    // salt for encoding
    private String salt;

    //real name
    private String name;

    private String phone;

    private String email;

    //user status
    private boolean enabled;

    //private List<> roles;
}
