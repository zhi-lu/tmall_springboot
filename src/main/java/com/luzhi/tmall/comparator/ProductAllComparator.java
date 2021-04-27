package com.luzhi.tmall.comparator;

import com.luzhi.tmall.pojo.Product;

import java.util.Comparator;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/2
 * 综合比较器
 * 销量*评论,谁大把其放在前面。。。。。。
 */
public class ProductAllComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return o2.getReviewCount() * o2.getSaleCount() -  o1.getSaleCount() + o1.getReviewCount();
    }
}
