package com.luzhi.tmall.dao;

import com.luzhi.tmall.pojo.Category;
import com.luzhi.tmall.pojo.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建一个产品的dao层
 */
public interface ProductDAO extends JpaRepository<Product, Integer> {
    /**
     * 获取关于Product 分类对象
     *
     * @param category 获取属性所对应的类型
     * @param pageable 一个Pageable对象. 用作规定排序方法和初始化界面位置
     * @return 返回有关Page<Product>对象..
     * @see #findByCategory(Category, Pageable)
     */
    Page<Product> findByCategory(Category category, Pageable pageable);

    /**
     * 前端:新增一个查询页面,只需要Category对象,不需要分页对象.
     *
     * @param category 获取对应的Category 对象
     * @return 返回产品列表.
     * @see #findByCategoryOrderById(Category)
     */
    List<Product> findByCategoryOrderById(Category category);

    /**
     * 模糊化查询,获取相关的Product对象
     *
     * @param keyword  相关的关键字
     * @param pageable 一个Pageable对象,规定相关的排序方法和其他等等....
     * @return 返回模糊化查询过后的Product对象。。。。
     * @see #findByNameLike(String, Pageable)
     */
    List<Product> findByNameLike(String keyword, Pageable pageable);
}
