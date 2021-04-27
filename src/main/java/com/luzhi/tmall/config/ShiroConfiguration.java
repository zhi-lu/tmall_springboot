package com.luzhi.tmall.config;

import com.luzhi.tmall.realm.JpaRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/15
 * 对Shiro进行具体化配置
 * 指定使用对Realm为{@link com.luzhi.tmall.realm.JpaRealm},对用户密码进行详细的加密
 * 使用加密方式为md5加密方式{@link org.apache.shiro.authc.credential.HashedCredentialsMatcher}.
 * 创建加密工厂{@link org.apache.shiro.spring.web.ShiroFilterFactoryBean}
 * 诸如如此.......
 */
@Configuration
public class ShiroConfiguration {


    /**
     * 保证在Shiro内部中生命周期函数Bean的执行...
     *
     * @see #getLifecycleBeanPostProcessor()
     */
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 配置Shiro的过滤器工厂类交给Spring工厂进行管理,通过一个安全管理类{@link SecurityManager}
     *
     * @see #shiroFilter(SecurityManager)
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        // 创建Shiro过滤器工厂
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 将shiro安全过滤器交给过滤器工厂..
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 返回这个配置好Shiro过滤器工厂
        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理{@link SecurityManager}
     * 配置并加载相关域,SecurityManager是Shiro的关键
     * 负责协调shiro的各个组件.......
     *
     * @see #securityManager()
     */
    @Bean
    public SecurityManager securityManager() {
        // 创建Web Shiro安全管理器
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 将自定义的域(Realm)交给Shiro安全管理器..
        defaultWebSecurityManager.setRealm(getJpaRealm());
        // 返回这个Shiro安全管理器
        return defaultWebSecurityManager;
    }

    /**
     * 设置本地域名{@link JpaRealm}
     * 配置加密方式.....
     *
     * @see #getJpaRealm()
     */
    @Bean
    public JpaRealm getJpaRealm() {
        // 创建自定义域(Realm)
        JpaRealm myShiroRealm = new JpaRealm();
        // 将凭证匹配器交给自定义(Realm)进行管理。。。
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        // 返回自定义域
        return myShiroRealm;
    }

    /**
     * @see #hashedCredentialsMatcher() 如何进行相关的加密
     * 详情请看
     * @see HashedCredentialsMatcher#setHashAlgorithmName(String)
     * @see HashedCredentialsMatcher#setHashIterations(int)
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {

        // 创建Hash凭证配置器..
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 设置凭证配置器的加密方式为md5的加密....
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        // 设置凭证配置器的加密次数(散列次数)为2
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     * 开启Shiro aop 代理模式
     * 使用代理方式需要开启代理
     *
     * @param securityManager 获取Shiro安全管理....
     * @see #authorizationAttributeSourceAdvisor(SecurityManager)
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
