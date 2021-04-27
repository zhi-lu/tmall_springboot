package com.luzhi.tmall.service;

import com.luzhi.tmall.dao.ReviewDAO;
import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.Review;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/5
 * 创建相关的"评论"服务层
 */
@Service
@SuppressWarnings("unused")
@CacheConfig(cacheNames = "reviews")
public class ReviewService {

    @Resource
    ReviewDAO reviewDao;

    @Resource
    ProductService productService;

    /**
     * @see #add(Review)
     * 添加评论
     */
    @CacheEvict(allEntries = true)
    public void add(Review review) {
        reviewDao.save(review);
    }

    /**
     * @see #list(Product)
     * 遍历相关产品的评论集合..
     */
    @Cacheable(key = "'reviews-pid-' + #p0.id")
    public List<Review> list(Product product) {
        return reviewDao.findByProductOrderByIdDesc(product);
    }

    /**
     * @see #getCount(Product)
     * 获取相关产品评论的数目。。
     */
    @Cacheable(key = "'reviews-count-pid' + #p0.id")
    public int getCount(Product product) {
        return reviewDao.countByProduct(product);
    }
}
