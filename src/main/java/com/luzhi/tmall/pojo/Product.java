package com.luzhi.tmall.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/1
 * 创建产品的pojo层
 * 使用@Document如何进行es匹配....
 */
@SuppressWarnings({"unused"})
@Entity
@Table(name = "product")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Document(indexName = "tmall_springboot", type = "product")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "subTitle")
    private String subTitle;

    @Column(name = "originalPrice")
    private float originalPrice;

    @Column(name = "promotePrice")
    private float promotePrice;

    @Column(name = "stock")
    private int stock;

    @ManyToOne
    @JoinColumn(name = "cid")
    private Category category;

    @Column(name = "createDate")
    private Date createDate;
    /**
     * @see #firstProductImage
     * 设置为非关联字段..
     */
    @Transient
    private ProductImage firstProductImage;

    /**
     * @see #productSingleImages
     * 此非关联属性是相关订单产品单个图片...
     */
    @Transient
    private List<ProductImage> productSingleImages;

    /**
     * @see #productDetailImages
     * 此非关联属性是相关订单产品详细图片的集合
     */
    @Transient
    private List<ProductImage> productDetailImages;
    /**
     * @see #saleCount
     * 此非关联属性是订单数量
     */
    @Transient
    private int saleCount;

    /**
     * @see #reviewCount
     * 此非关联属性意义是:产品回复数量..
     */
    @Transient
    private int reviewCount;

    public Category getCategory() {
        return category;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public float getPromotePrice() {
        return promotePrice;
    }

    public int getId() {
        return id;
    }

    public int getStock() {
        return stock;
    }

    public String getName() {
        return name;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public List<ProductImage> getProductDetailImages() {
        return productDetailImages;
    }

    public List<ProductImage> getProductSingleImages() {
        return productSingleImages;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public void setPromotePrice(float promotePrice) {
        this.promotePrice = promotePrice;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setFirstProductImage(ProductImage firstProductImage) {
        this.firstProductImage = firstProductImage;
    }

    public ProductImage getFirstProductImage() {
        return firstProductImage;
    }

    public void setProductDetailImages(List<ProductImage> productDetailImages) {
        this.productDetailImages = productDetailImages;
    }

    public void setProductSingleImages(List<ProductImage> productSingleImages) {
        this.productSingleImages = productSingleImages;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    /**
     * @see #toString()
     * 对Object类里的toString()方法进行重写.
     * 获取相关的属性
     */
    @Override
    public String toString() {
        return "Product {id:" + id + ", category:" + category + ", name:" + name + ",subTitle:" + subTitle
                + ", originalPrice:" + originalPrice + ", promotePrice:" + promotePrice + ", stock:" + ", firstProductImage:" + firstProductImage
                + ", saleCount:" + saleCount + ", reviewCount:" + reviewCount;
    }
}
