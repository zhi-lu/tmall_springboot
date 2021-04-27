package com.luzhi.tmall.comparator;

import com.luzhi.tmall.pojo.Product;

import java.util.Comparator;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/5
 * 创建销量比较器,销量高的放在前面....
 */
public class ProductSaleComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return o2.getSaleCount() - o1.getSaleCount();
    }
}
