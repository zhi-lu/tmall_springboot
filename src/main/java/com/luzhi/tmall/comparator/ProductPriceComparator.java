package com.luzhi.tmall.comparator;

import com.luzhi.tmall.pojo.Product;

import java.util.Comparator;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/3
 * 创建价格比较器,价格低廉放在前面......
 */
public class ProductPriceComparator implements Comparator<Product> {

    @Override
    public int compare(Product o1, Product o2) {
        return (int) (o1.getPromotePrice() - o2.getPromotePrice());
    }
}
