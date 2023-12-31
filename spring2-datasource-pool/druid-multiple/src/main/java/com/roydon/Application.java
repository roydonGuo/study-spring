package com.roydon;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Resource(name = "ordersDataSource")
    private DataSource ordersDataSource;

    @Resource(name = "usersDataSource")
    private DataSource usersDataSource;

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        // orders 数据源
        log.info("[run][获得数据源：{}]", ordersDataSource.getClass());

        // users 数据源
        log.info("[run][获得数据源：{}]", usersDataSource.getClass());
    }

}
