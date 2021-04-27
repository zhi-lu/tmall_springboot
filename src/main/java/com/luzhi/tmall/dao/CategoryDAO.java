package com.luzhi.tmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luzhi.tmall.pojo.Category;

/**
 * @author apple
 * @version jdk1.8
 * 创建一个 Category Dao层实例化持久对象API
 */
public interface CategoryDAO extends JpaRepository<Category, Integer> {
}
