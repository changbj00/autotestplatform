package com.autotestplatform.utils;

import com.autotestplatform.entity.ContextConst;
import com.autotestplatform.api.DataSourceSign;
import com.autotestplatform.hander.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *多数据源，切面处理类
 */
@Component
@Order(-1)// 保证该AOP在@Transactional之前执行
@Aspect
public class DynamicDataSourceAspect {
    @Before("execution(* com.autotestplatform.service..*.*(..))")
    public void before(JoinPoint point){
        try {
            DataSourceSign annotationOfClass =
                    point.getTarget().getClass().getAnnotation(DataSourceSign.class);

            String methodName = point.getSignature().getName();

            Class[] parameterTypes =
                    ((MethodSignature) point.getSignature()).getParameterTypes();

            Method method =
                    point.getTarget().getClass().getMethod(methodName, parameterTypes);

            DataSourceSign methodAnnotation =
                    method.getAnnotation(DataSourceSign.class);

            methodAnnotation = methodAnnotation ==
                    null ? annotationOfClass:methodAnnotation;

            ContextConst.DataSourceType dataSourceType =
                    methodAnnotation != null &&  methodAnnotation.value() !=null ?
                            methodAnnotation.value() :ContextConst.DataSourceType.PRIMARY ;
            DataSourceContextHolder.setDataSource(dataSourceType.name());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @After("execution(* com.autotestplatform.service..*.*(..))")
    public void after(JoinPoint point){
        DataSourceContextHolder.clearDataSource();
    }
}
