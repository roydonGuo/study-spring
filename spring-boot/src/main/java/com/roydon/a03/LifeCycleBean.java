package com.roydon.a03;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
@Component
public class LifeCycleBean {

    public LifeCycleBean() {
        log.info("1、构造");
    }

    @Autowired
    public void autowire(@Value("${JAVA_HOME}") String home) {
        log.info("2、依赖注入: {}", home);
    }

    @PostConstruct
    public void init() {
        log.info("3、初始化");
    }

    @PreDestroy
    public void destroy() {
        log.info("4、销毁");
    }

}
