package com.luzhi.tmall.dao;

import com.luzhi.tmall.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建一个用户的dao层
 */
public interface UserDAO extends JpaRepository<User, Integer> {

    /**
     * 用于前端发现用户名是否被使用
     *
     * @param name 获取前端输入的用户名
     * @return 返回 User对象
     * @see #findByName(String)
     */
    User findByName(String name);

    /**
     * 通过用户名和相关的密码获取相应的用户对象
     *
     * @param name     捕获相关的用户名
     * @param password 相对应的密码
     * @return User 返回对象
     * @see #getByNameAndPassword(String, String)
     */
    User getByNameAndPassword(String name, String password);
}
