package com.autotestplatform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

//排除DataSource自动配置类,否则会默认自动配置,不会使用我们自定义的DataSource,并且启动报错
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class AutotestplatformApplication implements CommandLineRunner,ApplicationContextAware {
    public static void main(String[] args) {
        SpringApplicationBuilder springApplicationBuilder=new SpringApplicationBuilder(AutotestplatformApplication.class);
        springApplicationBuilder.profiles("dev").logStartupInfo(true).run(args);
       // SpringApplication.run(AutotestplatformApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
