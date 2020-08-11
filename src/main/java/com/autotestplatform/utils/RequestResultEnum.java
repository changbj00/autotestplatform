package com.autotestplatform.utils;

public enum  RequestResultEnum {
    SUCCESS(10000, "处理成功"),
    FAILD(99999, "处理失败"),

    //服务层返回码
    SERVER_EXP(10001, "服务异常，请稍后重试！"),
    SERVER_BUSY(10005, "SERVER BUSY, 请稍后重试！"),


    //安全验证类返回码
    ILLEGAL_ACCESS(20001, "非法访问"),
    PARAMETER_IS_NULL(20002, "参数为空"),
    PARAMETER_IS_ERROR(20003, "参数格式不正确"),
    PARAMETER_UID_ERROE(20004, "uid为空或格式不对"),

    //业务类返回码
    login(1001, "用户名或密码错误"),
    register(1002,"用户已注册"),
    user(1003,"请检查用户信息是否全部填写"),
    forgot(1004,"用户未注册"),
    sendemail(1005,"验证码已发送，请稍后再试"),
    emailcode(1006,"验证码错误"),
    delete(1007,"用户不存在");

    private int code;
    private String msg;

    RequestResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public RequestResultEnum setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RequestResultEnum setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
