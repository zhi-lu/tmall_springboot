package com.luzhi.tmall.dao;

import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建产品图片的dao层
 */
public interface ProductImageDAO extends JpaRepository<ProductImage, Integer> {

    /**
     * 此方法用作查询对应的产品图片
     *
     * @param product 产品图片对应的产品
     * @param type    对应的类型是single还是detail
     * @return 返回一个List的列表.
     * @see #findByProductAndTypeOrderByIdDesc(Product, String)
     * 还是通过Jpa规则,按照一定的命名规则去查询信息.......
     */
    List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);
}
