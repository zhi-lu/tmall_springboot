package com.luzhi.tmall.comparator;

import com.luzhi.tmall.pojo.Product;

import java.util.Comparator;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/3
 * 创建时间筛选器,把新品放在前面
 */
public class ProductDateComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return o1.getCreateDate().compareTo(o2.getCreateDate());
    }
}
