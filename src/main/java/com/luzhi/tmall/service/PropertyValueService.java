package com.luzhi.tmall.service;

import com.luzhi.tmall.dao.PropertyValueDAO;
import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.Property;
import com.luzhi.tmall.pojo.PropertyValue;
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
 * 创建属性值的Service层
 * 先初始化,在提供查询和修改
 */
@Service
@CacheConfig(cacheNames = "propertyValues")
public class PropertyValueService {

    @Resource
    PropertyValueDAO propertyValueDao;

    @Resource
    PropertyService propertyService;

    /**
     * @see #update(PropertyValue)
     * 更改对象
     */
    @CacheEvict(allEntries = true)
    public void update(PropertyValue bean) {
        propertyValueDao.save(bean);
    }

    /**
     * @see #init(Product)
     * <p>
     * 进行初始化操作
     */
    public void init(Product product) {
        List<Property> propertyList = propertyService.listByCategory(product.getCategory());
        for (Property property : propertyList) {
            PropertyValue propertyValue = getPropertyAndProduct(property, product);
            // 如果为空,创建一个属性值,规定好其属性和产品.插入数据库中
            if (propertyValue == null) {
                propertyValue = new PropertyValue();
                propertyValue.setProduct(product);
                propertyValue.setProperty(property);
                propertyValueDao.save(propertyValue);
            }
        }
    }

    /**
     * @see #getPropertyAndProduct(Property, Product)
     * <p>
     * 通过Property和Product获取属性值
     */
    @Cacheable(key = "'propertyValues-one-pid-' + #p0.id + '-ptid-' + #p1.id")
    public PropertyValue getPropertyAndProduct(Property property, Product product) {
        return propertyValueDao.getByPropertyAndProduct(property, product);
    }

    /**
     * @see #findByProductOrderByIdDesc(Product)
     * <p>
     * 通过Product获取相关的属性List
     */
    @Cacheable(key = "'propertyValues-pid-' + #p0.id")
    public List<PropertyValue> findByProductOrderByIdDesc(Product product) {
        return propertyValueDao.findByProductOrderByIdDesc(product);
    }

    /**
     * @see #delete(PropertyValue)
     * 删除对象
     */
    @CacheEvict(allEntries = true)
    public void delete(PropertyValue bean) {

        propertyValueDao.delete(bean);
    }
}
