package com.autotestplatform.hander;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据源持有类,配合ThreadLocal存储数据源key
 * 保存线程安全的数据源
 * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
 * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
 */
@Slf4j
public class DataSourceContextHolder {
    private static final String DEFAULT_DATASOURCE = "PRIMARY_DATASOURCE";
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    /**
     * 设置数据源变量
     * @param dbType
     */
    public static void setDataSource(String dbType){
        log.info("切换到["+dbType+"]数据源");
        contextHolder.set(dbType);
    }

    /**
     * 获取数据源变量
     * @return
     */
    public static String getDataSource(){
        return contextHolder.get();
    }

    /**
     * 清空数据源变量
     */
    public static void clearDataSource(){
        contextHolder.remove();
    }
}
