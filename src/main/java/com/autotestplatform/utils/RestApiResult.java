package com.autotestplatform.utils;

import org.springframework.stereotype.Component;

/**
 * 统一返回响应数据：code+msg+data
 * @param <T>
 */
@Component
public class RestApiResult<T> {
    //响应码
    private Integer code;
    //响应消息
    private String msg;
    //结果数据
    private T data;

    public static RestApiResult faild() {
        return new RestApiResult(RequestResultEnum.FAILD.getCode(), RequestResultEnum.FAILD.getMsg(), null);
    }
    public  RestApiResult faild(T data) {
        return new RestApiResult(RequestResultEnum.FAILD.getCode(), RequestResultEnum.FAILD.getMsg(), data);
    }

    public static RestApiResult success() {
        return new RestApiResult(RequestResultEnum.SUCCESS.getCode(), RequestResultEnum.SUCCESS.getMsg(), null);
    }
    public RestApiResult success(T data) {
        return new RestApiResult(RequestResultEnum.SUCCESS.getCode(), RequestResultEnum.SUCCESS.getMsg(), data);
    }

    public static RestApiResult build(Integer code, String msg) {
        return new RestApiResult(code, msg, null);
    }


    public RestApiResult build(Integer code, String msg, T data) {
        return new RestApiResult(code, msg, data);
    }


    public RestApiResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public RestApiResult() {
    }

    public Integer getCode() {
        return code;
    }

    public RestApiResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RestApiResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestApiResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
