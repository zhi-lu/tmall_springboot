package com.luzhi.tmall.pojo;

import javax.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建一个关于Category类Pojo层.
 * // 使用@JsonIgnoreProperties注解,由于前后端数据交互依赖json。
 * 并使用jpa的实例类的持久化.忽略 handler,hibernateLazyInitialize这两个json化属性.
 */

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})

public class Category {

    /**
     * @see #id
     * 使用 GenerationType 为自增长型
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    String name;

    /**
     * @see #products
     * 分类下有多个产品
     */
    @Transient
    List<Product> products;

    /**
     * @see #productsByRow
     * 对应的分类下产品下的多行记录
     */
    @Transient
    List<List<Product>> productsByRow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<Product>> getProductsByRow() {
        return productsByRow;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setProductsByRow(List<List<Product>> productsByRow) {
        this.productsByRow = productsByRow;
    }
}
