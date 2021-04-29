package com.luzhi.tmall.dao;

import com.luzhi.tmall.pojo.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/16
 * 生成管理员adminUser的Dao层..
 */
public interface AdminUserDAO extends JpaRepository<AdminUser, Integer> {

    /**
     * 通过管理员和管理员密码获取管理员对象...
     *
     * @param name     获取管理员登录的用户名Name
     * @param password 获取管理员登录的密码 password;
     * @return 返回 AdminUser对象。
     * @see #getAdminUserByNameAndPassword(String, String)
     */
    AdminUser getAdminUserByNameAndPassword(String name, String password);
}
