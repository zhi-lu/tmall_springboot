package com.luzhi.tmall.dao;

import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.Property;
import com.luzhi.tmall.pojo.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 */
public interface PropertyValueDAO extends JpaRepository<PropertyValue, Integer> {

    /**
     * 此接口方法通过product获取产品属性值。
     * 使用Jpa 特定的命名大法,你准没有错.
     *
     * @param product 获取相关的产品属性
     * @return 返回关于属性值的List列表
     * @see #findByProductOrderByIdDesc(Product)
     */
    List<PropertyValue> findByProductOrderByIdDesc(Product product);

    /**
     * 此接口方法获取propertyValue属性
     * 使用Jpa命名法
     *
     * @param product  产品对象
     * @param property 属性对象.
     * @return PropertyValue 对象
     */
    PropertyValue getByPropertyAndProduct(Property property, Product product);
}
