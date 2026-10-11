package com.dormrepair.pojo;

import lombok.Data;

/**
 * 统一结果返回类
 * 用于封装接口返回结果，包含状态码、消息和数据。
 *
 * 约定：code=1 成功，code=0 失败。所有接口都返回这个格式，前端统一判断 code。
 */
@Data
public class Result {
    private Integer code;   // 编码：1成功 0失败
    private String msg;     // 提示信息
    private Object data;    // 数据

    /** 成功（不带数据） */
    public static Result success() {
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    /** 成功（带数据） */
    public static Result success(Object data) {
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        result.data = data;
        return result;
    }

    /** 失败 */
    public static Result error(String msg) {
        Result result = new Result();
        result.code = 0;
        result.msg = msg;
        return result;
    }
}
