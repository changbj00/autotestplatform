package com.autotestplatform.hander;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据源持有类,配合ThreadLocal存储数据源key
 * 保存线程安全的数据源
 */
@Slf4j
public class DataSourceContextHolder {
    private static final String DEFAULT_DATASOURCE = "PRIMARY_DATASOURCE";
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
    public static void setDataSource(String dbType){
        log.info("切换到["+dbType+"]数据源");
        contextHolder.set(dbType);
    }

    public static String getDataSource(){
        return contextHolder.get();
    }

    public static void clearDataSource(){
        contextHolder.remove();
    }
}
