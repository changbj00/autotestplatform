package com.autotestplatform.api;

import com.autotestplatform.entity.ContextConst;

import java.lang.annotation.*;

/**
 * 定义切换数据源的注解和切面,实现数据源的动态切换
 * springboot有提供AbstractRoutingDataSource#determineCurrentLookupKey抽象方法去指定数据源,我们要做的就是实现切换数据源的逻辑;通过AOP在调用数据库之前切换数据源;
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceSign {
    ContextConst.DataSourceType value() default ContextConst.DataSourceType.PRIMARY;

}
