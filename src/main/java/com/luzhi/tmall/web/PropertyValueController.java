package com.luzhi.tmall.web;

import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.PropertyValue;
import com.luzhi.tmall.service.ProductService;
import com.luzhi.tmall.service.PropertyValueService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建属性值控制类,好累呀...............
 */
@RestController
public class PropertyValueController {

    @Resource
    ProductService productService;

    @Resource
    PropertyValueService propertyValueService;

    /**
     * @see #list(int)
     * <p>
     * 获取产品下的属性值..
     */
    @GetMapping("/products/{pid}/propertyValues")
    public List<PropertyValue> list(@PathVariable("pid") int pid) {
        Product product = productService.get(pid);
        propertyValueService.init(product);
        return propertyValueService.findByProductOrderByIdDesc(product);
    }

    /**
     * @see #update(PropertyValue)
     * <p>
     * 更新产品属性值
     */
    @PutMapping("/propertyValues")
    public Object update(@RequestBody PropertyValue bean) {

        String content = bean.getValue().trim();
        // 返回空字符串进行删除
        if ("".equals(content)) {
            propertyValueService.delete(bean);
            return null;
        } else {
            propertyValueService.update(bean);
            return bean;
        }
    }
}
