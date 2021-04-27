package com.luzhi.tmall.service;

import java.util.List;

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

import com.luzhi.tmall.dao.CategoryDAO;
import com.luzhi.tmall.pojo.Category;

import javax.annotation.Resource;

/**
 * @author apple
 * @version jdk8
 * // TODO : 2021/3/21
 * 创建service{@link Service}层对dao层功能进行实现。。。
 * 创建redis Cache {@link CacheConfig} 缓存,缓存中所有的keys都在categories中
 * 详细使用相关的redis随后来说:
 * 在Service层中的add和update方法可以使用{@link org.springframework.cache.annotation.CachePut}
 * {@code ("'categories-one-' + #p0")},但这里我是使用{@link CacheEvict}{@code (allEntries=true)}
 * 在这里进行删除,添加,修改都会导致数据在redis改变中,但{@code @Cacheable(key = "'categories-page-' + #p0 + '-' + #p1")}
 * 分页数据就会不统一。在进行这些操作时,会删除在redis中的数据.下次访问时查看redis中没有数据就会重新的从数据库中获取数据.
 * 牺牲类一点性能.保证了数据的一致性.
 */
@Service
@CacheConfig(cacheNames = "categories")
public class CategoryService {

    /**
     * @see #categoryDAO
     * // 使用@Resoure注解@Resoure应用的范围大于@AutoWired
     * // 这也是我为什么使用jdk1.8, Spring支持j2ee的写法.
     * // jdk的高版本对j2ee的支持不友好.
     */
    @Resource
    CategoryDAO categoryDAO;

    /**
     * @see #list(int, int, int)
     * // 对数据进行逆排序
     */
    @Cacheable(key = "'categories-page-' + #p0 + '-' + #p1")
    public Page4Navigator<Category> list(int start, int size, int navigatePages) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Category> pageFromJpa = categoryDAO.findAll(pageable);

        return new Page4Navigator<>(pageFromJpa, navigatePages);
    }

    /**
     * @see #list()
     * 获取数据库改对象的当前全部数据.
     */
    @Cacheable(key = "'categories-all'")
    public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    /**
     * @see #add(Category)
     * 添加对象
     */
    @CacheEvict(allEntries = true)
    public void add(Category bean) {
        categoryDAO.save(bean);
    }

    /**
     * @see #delete(int)
     * 删除对象通过对象id
     */
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        categoryDAO.delete(id);
    }

    /**
     * @see #get(int)
     * 通过id获取单个对象
     */
    @Cacheable(key = "'categories-one-' + #p0")
    public Category get(int id) {
        return categoryDAO.findOne(id);
    }

    /**
     * @see #update(Category)
     * 对数据进行更新.
     */
    @CacheEvict(allEntries = true)
    public void update(Category bean) {
        categoryDAO.save(bean);
    }

    /**
     * @see #removeCategoryFromProduct(List)
     * 删除Category 列表Cateory 属性 category 对象下的 product 值的Category
     */
    public void removeCategoryFromProduct(List<Category> categoryList) {
        for (Category category : categoryList) {
            removeCategoryFromProduct(category);
        }
    }

    /**
     * @see #removeCategoryFromProduct(Category)
     * 具体实现,自己写丰衣足食.
     * 不使用@JsonIgnoreProperties注解, 在对redis整合时会出现一些问题........
     */
    public void removeCategoryFromProduct(@org.jetbrains.annotations.NotNull Category category) {
        List<Product> products = category.getProducts();
        // 判断product不为空
        if (products != null) {
            for (Product product : products) {
                product.setCategory(null);
            }
        }

        List<List<Product>> productByRow = category.getProductsByRow();
        if (productByRow != null) {
            for (List<Product> productList : productByRow) {
                for (Product product : productList) {
                    product.setCategory(null);
                }
            }
        }
    }
}
