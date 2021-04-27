package com.luzhi.tmall.web;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luzhi.tmall.pojo.Property;
import com.luzhi.tmall.service.PropertyService;
import com.luzhi.tmall.util.Page4Navigator;


/**
 * @author apple
 * @version jdk8
 * // TODO : 2021/3/21
 * 属性控制层,属性与分类的关系为 ManyToOne;
 * 还是那样 REST....
 */
@RestController
public class PropertyController {

    @Resource
    PropertyService propertyService;

    /**
     * @see #list(int, int, int)
     * <p style="color:pink">
     * 对属性进行逆排序..</p>
     */
    @GetMapping("/categories/{cid}/properties")
    public Page4Navigator<Property> list(@PathVariable("cid") int cid,
                                         @RequestParam(value = "start", defaultValue = "0") int start,
                                         @RequestParam(value = "size", defaultValue = "5") int size) {
        start = Math.max(0, start);
        return propertyService.list(cid, start, size, 5);
    }

    /**
     * @see #get(int)
     * <p style="color:pink">
     * 通过id获取Property对象
     * </p>
     */
    @GetMapping("/properties/{id}")
    public Property get(@PathVariable("id") int id) throws Exception {
        return propertyService.get(id);
    }

    /**
     * @see #add(Property)
     * <p style="color:blue">
     * 保存对象
     * </p>
     */
    @PostMapping("/properties")
    public Object add(@RequestBody Property bean) throws Exception {
        propertyService.add(bean);
        return bean;
    }

    /**
     * @see #delete(int, HttpServletRequest)
     * <p style="color:lightpink">
     * 删除对象
     * </p>
     */
    @DeleteMapping("/properties/{id}")
    public String delete(@PathVariable("id") int id, @SuppressWarnings("unused") HttpServletRequest request) throws Exception {
        propertyService.delete(id);
        return null;
    }

    /**
     * @see #update(Property)
     *
     * <p>
     * 更新对象...
     * </p>
     */
    @PutMapping("/properties")
    public Object update(@RequestBody Property bean) throws Exception {
        propertyService.update(bean);
        return bean;
    }
}
