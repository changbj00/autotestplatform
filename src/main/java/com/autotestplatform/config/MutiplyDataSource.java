package com.autotestplatform.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.autotestplatform.entity.ContextConst;
import com.autotestplatform.utils.DynamicDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * 定义数据源（在工程的config包下面）
 */
@Configuration
public class MutiplyDataSource {
    //@Primary注解在哪个ds，默认使用那个ds
    @Bean(name = "dataSourcePrimary")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource(){
        return new DruidDataSource();
    }
    @Bean(name = "dataSourceLocal")
    @ConfigurationProperties(prefix = "spring.datasource.local")
    public DataSource localDataSource(){
        return new DruidDataSource();
    }

    @Primary
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //配置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(primaryDataSource());

        //配置多数据源
        HashMap<Object, Object> dataSourceMap = new HashMap();
        dataSourceMap.put(ContextConst.DataSourceType.PRIMARY.name(),primaryDataSource());
        dataSourceMap.put(ContextConst.DataSourceType.LOCAL.name(),localDataSource());
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    /**
     * 配置@Transactional注解事务
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}
