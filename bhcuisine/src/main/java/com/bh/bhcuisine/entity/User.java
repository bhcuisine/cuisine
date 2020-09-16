package com.bh.bhcuisine.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/9/16
 */

@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class User {
    private String username;
}
