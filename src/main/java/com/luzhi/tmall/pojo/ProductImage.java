package com.luzhi.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建产品图片的pojo层
 */

@Entity
@Table(name = "productImage")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class ProductImage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "pid")
    @JsonBackReference
    private Product product;

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setType(String type) {
        this.type = type;
    }
}
