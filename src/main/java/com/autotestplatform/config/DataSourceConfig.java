package com.autotestplatform.config;

import com.autotestplatform.entity.DataSourceType;
import com.autotestplatform.utils.DynamicDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * 定义数据源（在工程的config包下面）
 */
@Configuration
public class DataSourceConfig {
    //@master注解在哪个ds，默认使用那个ds
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
        //return new DruidDataSource();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
        //return new DruidDataSource();
    }

    @Primary
    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource(DataSource masterDataSource, DataSource slaveDataSource) {
//        DynamicDataSource dynamicDataSource = new DynamicDataSource(masterDataSource);
//        //配置默认数据源
//        dynamicDataSource.setDefaultTargetDataSource(masterDataSource());

        //配置多数据源
        HashMap<Object, Object> dataSourceMap = new HashMap();
        dataSourceMap.put(DataSourceType.master.name(), masterDataSource());
        dataSourceMap.put(DataSourceType.slave.name(), slaveDataSource());
        return new DynamicDataSource(masterDataSource, dataSourceMap);
    }

//    /**
//     * 配置@Transactional注解事务
//     * @return
//     */
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        return new DataSourceTransactionManager(dynamicDataSource());
//    }
}
