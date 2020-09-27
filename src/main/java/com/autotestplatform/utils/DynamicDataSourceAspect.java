package com.autotestplatform.utils;

import com.autotestplatform.api.DataSource;
import com.autotestplatform.hander.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *多数据源，切面处理类
 * 通过拦截上面的注解，在其执行之前处理设置当前执行SQL的数据源的信息，
 * CONTEXT_HOLDER.set(dataSourceType)这里的数据源信息从我们设置的注解上面获取信息，
 * 如果没有设置就是用默认的数据源的信息。
 */
@Component
@Order(1)// 保证该AOP在@Transactional之前执行
@Aspect
public class DynamicDataSourceAspect {
    @Pointcut("@annotation(com.autotestplatform.api.DataSource)")
    public void dsPointCut(){

    }
    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource != null) {
            DataSourceContextHolder.setDataSource(dataSource.value().name());
        }
        try {
            return point.proceed();
        } finally {
            // 销毁数据源 在执行方法之后
            DataSourceContextHolder.clearDataSource();
        }
    }
    /*@Before("execution(* com.autotestplatform.service..*.*(..))")
    public void before(JoinPoint point){
        try {
            DataSource annotationOfClass =
                    point.getTarget().getClass().getAnnotation(DataSource.class);

            String methodName = point.getSignature().getName();

            Class[] parameterTypes =
                    ((MethodSignature) point.getSignature()).getParameterTypes();

            Method method =
                    point.getTarget().getClass().getMethod(methodName, parameterTypes);

            DataSource methodAnnotation =
                    method.getAnnotation(DataSource.class);

            methodAnnotation = methodAnnotation ==
                    null ? annotationOfClass:methodAnnotation;

            DataSourceType dataSourceType =
                    methodAnnotation != null &&  methodAnnotation.value() !=null ?
                            methodAnnotation.value() :DataSourceType.master ;
            DataSourceContextHolder.setDataSource(dataSourceType.name());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @After("execution(* com.autotestplatform.service..*.*(..))")
    public void after(JoinPoint point){
        DataSourceContextHolder.clearDataSource();
    }*/
}
