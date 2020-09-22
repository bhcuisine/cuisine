package com.bh.bhcuisine.result;

/**
 * @Description:返回对象工厂
 * @Author: YTF
 * @Date: 2020/8/7
 */
public class ResultFactory {

    //成功返回，并带回所需数据
    public static Result buildSuccessResult(Object data) {
        return buildResult(ResultCode.SUCCESS, "成功", data);
    }

    //结果失败，显示相关提示信息
    public static Result buildFailResult(String message) {
        return buildResult(ResultCode.FAIL, message, null);
    }

    public static Result buildResult(ResultCode resultCode, String message, Object data) {
        return buildResult(resultCode.code, message, data);
    }

    public static Result buildResult(int resultCode, String message, Object data) {
        return new Result(resultCode, message, data);
    }
}
