package com.autotestplatform.hander;

import com.autotestplatform.utils.RequestResultEnum;

/**
 * 统一异常处理类，返回统一格式数据
 */
public class CatchException extends RuntimeException {
    private final RequestResultEnum resultEnum;

    public CatchException(RequestResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }
    public RequestResultEnum getResultEnum(){
        return resultEnum;
    }
}
