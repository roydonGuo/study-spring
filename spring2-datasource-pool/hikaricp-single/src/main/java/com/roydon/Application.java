package com.roydon;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        try (Connection conn = dataSource.getConnection()) {
            // 这里，可以做点什么
            log.info("[run][获得连接：{}]", conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
