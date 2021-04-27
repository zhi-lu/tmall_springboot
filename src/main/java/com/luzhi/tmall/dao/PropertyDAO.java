package com.luzhi.tmall.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.luzhi.tmall.pojo.Category;
import com.luzhi.tmall.pojo.Property;

import java.util.List;


/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 生成Property 的dao层
 */
public interface PropertyDAO extends JpaRepository<Property, Integer> {
    /**
     * 获取对象类Page<Property>
     *
     * @param category 获得一个分类对象
     * @param pageable 获取通过 start size 排序方法 的 Pageable 对象
     * @return Page<Property> 序列化对象..
     * @see #findByCategory(Category, Pageable)
     * 此方法命名是通过Jpa规则,按照一定的命名规则去查询信息.......
     */
    Page<Property> findByCategory(Category category, Pageable pageable);

    /**
     * 通过Category获取所有属性集合的方法
     *
     * @param category 通过Category 对象属性.
     * @return List<Property> List对象
     */
    List<Property> findByCategory(Category category);
}