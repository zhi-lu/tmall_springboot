package com.luzhi.tmall.service;

import com.luzhi.tmall.dao.ProductDAO;
import com.luzhi.tmall.pojo.Category;
import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.util.Page4Navigator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建产品的service层,超级组合拳头。CRUD
 */
@Service
@CacheConfig(cacheNames = "products")
public class ProductService {

    @Resource
    ProductDAO productDao;


    @Resource
    CategoryService categoryService;

    @Resource
    ProductImageService productImageService;

    @Resource
    OrderItemService orderItemService;

    @Resource
    ReviewService reviewService;

    /**
     * @see #get(int)
     * 找
     */
    @Cacheable(key = "'products-one-' + #p0")
    public Product get(int id) {
        return productDao.findOne(id);
    }

    /**
     * @see #update(Product)
     * 改
     */
    @CacheEvict(allEntries = true)
    public void update(Product bean) {
        productDao.save(bean);
    }

    /**
     * @see #delete(int)
     * 删
     */
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        productDao.delete(id);
    }

    /**
     * @see #add(Product)
     * 添
     */
    @CacheEvict(allEntries = true)
    public void add(Product bean) {
        productDao.save(bean);
    }

    /**
     * 获取排序好对象
     *
     * @see #list(int, int, int, int)
     */
    @Cacheable(key = "'products-cid-' + #p0 + '-page-' + #p1 + '-' + #p2")
    public Page4Navigator<Product> list(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Product> pageFromJpa = productDao.findByCategory(category, pageable);
        return new Page4Navigator<>(pageFromJpa, navigatePages);
    }

    /**
     * @see #fill(List)
     * 遍历对象,为多个分类填充对象。
     */
    public void fill(List<Category> categoryList) {
        for (Category category : categoryList) {
            fill(category);
        }
    }

    /**
     * @see #fill(Category)
     * 为相关的分类填充对象
     */
    public void fill(Category category) {
        List<Product> products = findByCategoryOrderId(category);
        productImageService.setFirstProductImages(products);
        category.setProducts(products);
    }

    /**
     * @see #fill(Category)
     * 为相关的分类提供推荐产品;
     */
    public void fillByRow(List<Category> categoryList) {
        // 每行为7个推荐产品.
        int productNumberEachRow = 7;
        for (Category category : categoryList) {
            List<Product> products = category.getProducts();
            List<List<Product>> productByRow = new ArrayList<>();
            for (int i = 0; i < products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = Math.min(size, products.size());
                List<Product> productsOfEachRow = products.subList(i, size);
                productByRow.add(productsOfEachRow);
            }
            category.setProductsByRow(productByRow);
        }
    }

    /**
     * @see #findByCategoryOrderId(Category)
     * 实现dao层,Jpa自定义方法..返回产品列表..
     */
    @Cacheable(key = "'products-cid-' + #p0.id")
    public List<Product> findByCategoryOrderId(Category category) {
        return productDao.findByCategoryOrderById(category);
    }

    /**
     * @see #setSaleAndReviewNumber(Product)
     * 设置单个产品对象的出售和评论数量..
     */
    public void setSaleAndReviewNumber(Product product) {

        int saleCount = orderItemService.getSaleCount(product);
        product.setSaleCount(saleCount);

        int reviewCount = reviewService.getCount(product);
        product.setReviewCount(reviewCount);
    }

    /**
     * @see #setSaleAndReviewNumber(List)
     * 设置Product产品的集合获取相关的出售和评论数量
     */
    public void setSaleAndReviewNumber(List<Product> productList) {
        for (Product product : productList) {
            setSaleAndReviewNumber(product);
        }
    }

    /**
     * @see #search(String, int, int)
     * 具体实现如何模糊化查询,我这里是使用了es.但是mac springBoot版本没有对应的es
     * 写好放在这里.
     */
    public List<Product> search(String keyword, int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        return productDao.findByNameLike("%" + keyword + "%", pageable);
    }
}
