package com.luzhi.tmall.dao;

import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/11
 * 创建一个关于评论的dao层.....
 */
public interface ReviewDAO extends JpaRepository<Review, Integer> {

    /**
     * 此接口方法用作返回对某个产品对综合评价
     *
     * @param product 通过相关对Product对象获取相关对评论..
     * @return 返回相关对评论集合List<Object>
     * @see #findByProductOrderByIdDesc(Product)
     */
    List<Review> findByProductOrderByIdDesc(Product product);

    /**
     * 此接口方法返回相关产品对应对评论数量
     *
     * @param product 通过相关对Product对象..
     * @return 返回相关的评论数量..
     * @see #countByProduct(Product)..
     */
    int countByProduct(Product product);
}
