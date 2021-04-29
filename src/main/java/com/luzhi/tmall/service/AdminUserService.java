package com.luzhi.tmall.service;

import com.luzhi.tmall.dao.AdminUserDAO;
import com.luzhi.tmall.pojo.AdminUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/15
 * 生成管理员的服务层,由于是相关的管理员只提供查找功能
 * 不提供删除,修改,添加功能.......
 */
@Service
public class AdminUserService {

    @Resource
    AdminUserDAO adminUserDao;

    /**
     * 通过Controller层 传来的管理员用户名name和相关password
     * 获取管理员对象. 具体方法为:{@link AdminUserDAO#getAdminUserByNameAndPassword(String, String)}
     * @see #getAdminUser(String, String)
     */
    public AdminUser getAdminUser(String name, String password) {
        return adminUserDao.getAdminUserByNameAndPassword(name, password);
    }
}
