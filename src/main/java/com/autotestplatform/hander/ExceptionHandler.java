package com.autotestplatform.hander;

import com.autotestplatform.utils.RequestResultEnum;
import com.autotestplatform.utils.RestApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * 异常处理全局匹配类
 * 如果exception类型是自定义的异常StudentException，直接抛出，如果是其它异常统一抛出网络错误
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(CatchException.class)
    public RestApiResult handlerException(HttpServletRequest request,CatchException ce){
        RestApiResult restApiResult;
        log.error("CatchException code{},msg{}",ce.getResultEnum().getCode(),ce.getResultEnum().getMsg());
        restApiResult=new RestApiResult(ce.getResultEnum().getCode(),ce.getResultEnum().getMsg(),null);
        return restApiResult;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public RestApiResult handleException(HttpServletRequest request, Exception ex) {
        RestApiResult response;
        log.error("exception error:{}",ex);
        response = new RestApiResult(RequestResultEnum.SERVER_EXP.getCode(),
                RequestResultEnum.SERVER_EXP.getMsg(),null);
        return response;
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
    public RestApiResult handleException(HttpServletRequest request, ConstraintViolationException cve) {
        RestApiResult response;
        log.error("exception error:{}",cve);
        response = new RestApiResult(RequestResultEnum.PARAMETER_IS_ERROR.getCode(),
                cve.getMessage().split(":")[1],null);
        return response;
    }

}
