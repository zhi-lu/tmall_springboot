package com.luzhi.tmall.config;

import com.luzhi.tmall.interceptor.AdminLoginInterceptor;
import com.luzhi.tmall.interceptor.LoginInterceptor;
import com.luzhi.tmall.interceptor.OtherInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/11
 * 生成WebMvc配置拦截器.....
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    /**
     * @see #getLoginInterceptor()
     * 设置需求"登录"拦截器
     */
    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

    /**
     * @see #getAdminLoginInterceptor()
     * 后端访问"登录"拦截器..
     */
    @Bean
    public AdminLoginInterceptor getAdminLoginInterceptor() {
        return new AdminLoginInterceptor();
    }

    /**
     * @see #getOtherInterceptor()
     * 设置session和application拦截器..
     */
    @Bean
    public OtherInterceptor getOtherInterceptor() {
        return new OtherInterceptor();
    }

    /**
     * @see #addInterceptors(InterceptorRegistry)
     * 添加拦截器,这是不可少的.在相关的Web中{@link Bean}实现完
     * 拦截器是继承方法{@link WebMvcConfigurerAdapter#addInterceptors(InterceptorRegistry)}  }
     * 把拦截器"注册"成功,可以实现相关的拦截功能.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor()).
                addPathPatterns("/**");
        registry.addInterceptor(getOtherInterceptor()).
                addPathPatterns("/**");
        registry.addInterceptor(getAdminLoginInterceptor()).
                addPathPatterns("/**");
    }
}
