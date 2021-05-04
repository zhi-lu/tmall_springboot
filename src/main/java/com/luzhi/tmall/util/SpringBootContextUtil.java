package com.luzhi.tmall.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/23,
 * 诱发springboot缓存机制进行切片面向编程aop.
 */
@Component
public class SpringBootContextUtil implements ApplicationContextAware {

    private SpringBootContextUtil() {

    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBootContextUtil.applicationContext = applicationContext;
    }

    @SuppressWarnings("unused")
    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}
