package com.luzhi.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 */
@Entity
@Table(name = "propertyValue")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class PropertyValue {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value")
    private String value;

    @SuppressWarnings("SpellCheckingInspection")
    @ManyToOne
    @JoinColumn(name = "ptid")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Product product;

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public Property getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "propertyValue { id : " + id + ",property:" + property +
                ",product:" + property + ",value:" + value + ",}";
    }
}
