package com.luzhi.tmall.web;

import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.service.ProductImageService;
import com.luzhi.tmall.service.ProductService;
import com.luzhi.tmall.util.Page4Navigator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 创建产品的控制类
 */
@RestController
public class ProductController {

    @Resource
    ProductService productService;

    @Resource
    ProductImageService productImageService;

    /**
     * @see #list(int, int, int)
     * 老样子,返回一个定义好的序列号对象..
     */
    @GetMapping("/categories/{cid}/products")
    public Page4Navigator<Product> list(@PathVariable("cid") int cid, @RequestParam(value = "start", defaultValue = "0") int start,
                                        @RequestParam(value = "size", defaultValue = "5") int size) {

        start = Math.max(0, start);
        Page4Navigator<Product> page = productService.list(cid, start, size, 5);
        productImageService.setFirstProductImages(page.getContent());
        return page;
    }

    /**
     * @see #get(int)
     * 查找放回一个Product对象
     */
    @GetMapping("/products/{id}")
    public Product get(@PathVariable("id") int id) {
        return productService.get(id);
    }

    /**
     * @see #add(Product)
     * 添加对象
     */
    @PostMapping("/products")
    public Object add(@RequestBody Product bean) {
        productService.add(bean);
        return bean;
    }

    /**
     * @see #delete(int, HttpServletRequest)
     * 删除对象
     */
    @DeleteMapping("/products/{id}")
    public String delete(@PathVariable("id") int id, @SuppressWarnings("unused") HttpServletRequest request) {
        productService.delete(id);
        return null;
    }

    /**
     * @see #update(Product)
     * 更新对象
     */
    @PutMapping("/products")
    public Object update(@RequestBody Product bean) throws Exception {
        productService.update(bean);
        return bean;
    }
}
