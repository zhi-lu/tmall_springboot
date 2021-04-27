package com.luzhi.tmall.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.luzhi.tmall.dao.PropertyDAO;
import com.luzhi.tmall.pojo.Category;
import com.luzhi.tmall.pojo.Property;
import com.luzhi.tmall.util.Page4Navigator;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk8
 * // TODO : 2021/3/21
 * 有时CRUD那一套
 */
@Service
@CacheConfig(cacheNames = "properties")
public class PropertyService {

    @Resource
    PropertyDAO propertyDao;

    @Resource
    CategoryService categoryService;

    /**
     * 通过id 查找对象
     *
     * @see #get(int)
     */
    @Cacheable(key = "'properties-one-' + #p0")
    public Property get(int id) {
        return propertyDao.findOne(id);
    }

    /**
     * 更改属性
     *
     * @see #update(Property)
     */
    @CacheEvict(allEntries = true)
    public void update(Property bean) {
        propertyDao.save(bean);
    }

    /**
     * 删除对象
     *
     * @see #delete(int)
     */
    @CacheEvict(allEntries = true)
    public void delete(int id) {
        propertyDao.delete(id);
    }

    /**
     * 添加对象
     *
     * @see #delete(int)
     */
    @CacheEvict(allEntries = true)
    public void add(Property bean) {
        propertyDao.save(bean);
    }

    /**
     * 获取分类下的对象
     *
     * @see #list(int, int, int, int)
     */
    @Cacheable(key = "'properties-cid-' + #p0 + '-page-' + #p1 + '-' +#p2")
    public Page4Navigator<Property> list(int cid, int start, int size, int navigatePages) {
        Category category = categoryService.get(cid);
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);

        Page<Property> pageFromJpa = propertyDao.findByCategory(category, pageable);
        return new Page4Navigator<>(pageFromJpa, navigatePages);
    }

    /**
     * 增加通过分类获取所有属性集合的方法
     *
     * @see #listByCategory(Category)
     */
    @Cacheable(key = "'properties-cid-' + #p0.id")
    public List<Property> listByCategory(Category category) {
        return propertyDao.findByCategory(category);
    }
}