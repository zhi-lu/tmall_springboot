package com.luzhi.tmall.service;

import com.luzhi.tmall.dao.UserDAO;
import com.luzhi.tmall.pojo.User;
import com.luzhi.tmall.util.Page4Navigator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 提供User的Service层
 */
@Service
@CacheConfig(cacheNames = "users")
public class UserService {

    @Resource
    UserDAO userDao;


    /**
     * @see #isExist(String)
     * 获取用户名判断是否存在。
     */
    public boolean isExist(String name) {
        return null != getByName(name);
    }

    /**
     * @see #getByName(String)
     * 发现用户名对象所对应的User 对象
     */
    @Cacheable(key = "'users-one-name-' + #p0")
    public User getByName(String name) {
        return userDao.findByName(name);
    }

    /**
     * @see #get(String, String)
     * 实现相关的dao层接口方法,查询对象
     */
    @Cacheable(key = "'users-one-name-' + #p0 + '-password-' + #p1")
    public User get(String name, String password) {
        return userDao.getByNameAndPassword(name, password);
    }

    /**
     * @see #add(User)
     * 添加对象..
     */
    @CacheEvict(allEntries = true)
    public void add(User user) {
        userDao.save(user);
    }

    /**
     * @see #list(int, int, int)
     * 提供查询
     */
    @Cacheable(key = "'users-page-' + #p0 + '-' + #p1")
    public Page4Navigator<User> list(int start, int size, int navigatePages) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<User> userFormJpa = userDao.findAll(pageable);
        return new Page4Navigator<>(userFormJpa, navigatePages);
    }
}
