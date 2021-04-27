package com.luzhi.tmall;

import com.luzhi.tmall.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author apple
 * @version jdk8
 * // TODO : 2021/3/21
 * 设置SpringBoot {@link SpringBootApplication} 启动器...
 * 有人问 SpringBoot 和 SpringMvc 的区别
 * SpringBoot 相对与老鸨负责招揽客人,淦活的还是SpringMvc..........
 * 使用注解 {@link EnableCaching} 开启redis缓存...
 * 开启搜索引擎 {@link EnableElasticsearchRepositories} 作用于{@link EnableJpaRepositories}下的包
 */

@EnableCaching
@SpringBootApplication
public class Application {

    // SpringBoot启动预先加载相关到静态属性.查看redis,elasticSearch和Kibana¬服务是否打开.
    static {
        PortUtil.checkPort(6379, "redis服务", true);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
