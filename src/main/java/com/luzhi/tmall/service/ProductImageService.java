package com.luzhi.tmall.service;

import com.luzhi.tmall.dao.ProductImageDAO;
import com.luzhi.tmall.pojo.OrderItem;
import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.ProductImage;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建一个产品的service层
 */
@Service
@SuppressWarnings("unused")
@CacheConfig(cacheNames = "productImages")
public class ProductImageService {

    @Resource
    ProductImageDAO productImageDao;

    @Resource
    ProductService productService;

    /**
     * @see #TYPE_SINGLE
     * 查看单个图片
     */
    public final static String TYPE_SINGLE = "single";
    /**
     * @see #TYPE_DETAIL
     * 查看多组图片
     */
    public final static String TYPE_DETAIL = "detail";

    /**
     * @see #get(int)
     * 查找.
     */
    @Cacheable(key = "'productImages-one-' + #p0")
    public ProductImage get(int id) {
        return productImageDao.findOne(id);
    }

    /**
     * @see #delete(int)
     * 删除
     */
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        productImageDao.delete(id);
    }

    /**
     * @see #add(ProductImage)
     * 添加对象
     */
    @CacheEvict(allEntries = true)
    public void add(ProductImage bean) {
        productImageDao.save(bean);
    }

    /**
     * @see #listSingleProductImage(Product)
     * 获取单个图片
     */
    @Cacheable(key = "'productImages-single-pid-' + #p0.id")
    public List<ProductImage> listSingleProductImage(Product product) {
        return productImageDao.findByProductAndTypeOrderByIdDesc(product, TYPE_SINGLE);
    }

    /**
     * @see #listDetailProductImages(Product)
     * 获取多组图片
     */
    @Cacheable(key = "'productImages-detail-pid-' + #p0.id")
    public List<ProductImage> listDetailProductImages(Product product) {
        return productImageDao.findByProductAndTypeOrderByIdDesc(product, TYPE_DETAIL);
    }

    public void setFirstProductImage(Product product) {

        List<ProductImage> singleProductImage = listSingleProductImage(product);
        if (!singleProductImage.isEmpty()) {
            product.setFirstProductImage(singleProductImage.get(0));
        } else {
            // 如果!singleProductImage List为空
            product.setFirstProductImage(new ProductImage());
        }
    }

    /**
     * @see #setFirstProductImages(List)
     * 捕获产品图片说对应的产品对象
     */
    public void setFirstProductImages(List<Product> products) {
        for (Product product : products) {
            setFirstProductImage(product);
        }
    }

    /**
     * @see #setFirstProductImagesOnOrderItems(List)
     * 为每一个订单设置相关的图片属性.....
     */
    public void setFirstProductImagesOnOrderItems(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            setFirstProductImage(orderItem.getProduct());
        }
    }
}
