package com.bh.bhcuisine.result;

import lombok.Data;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/7/31
 */
@Data
public class Result {

    private int code;

    private String message;

    private Object result;

    public Result(int code) {
        this.code = code;
    }

    public Result(int code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
