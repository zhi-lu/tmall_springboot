package com.luzhi.tmall.comparator;

import com.luzhi.tmall.pojo.Product;

import java.util.Comparator;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/3
 * 创建评论比价器,评论多的放在前面.....
 */
public class ProductReviewComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return o2.getReviewCount() - o1.getReviewCount();
    }
}
