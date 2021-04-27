package com.luzhi.tmall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author apple
 * @version jdk8
 * // TODO : 2021/3/21
 * 生成一个Controller 控制类
 * 使用Restful 指定路径..
 */

@Controller
public class AdminPageController {

    /**
     * @see #admin()
     * 访问 localHost:port/amdin 通过Restful Mapping
     * 调整到../admin_category_list
     */
    @GetMapping(value = "/admin")
    public String admin() {
        return "redirect:admin_category_list";
    }

    /**
     * @see #listCategory()
     * 访问上面返回的../ admin_category_list 跳转到listCategory.html页面
     */
    @GetMapping(value = "/admin_category_list")
    public String listCategory() {
        return "admin/listCategory";
    }

    /**
     * @see #editCategory()
     * 访问修改../admin_category_edit
     * 映射到editCategory.html
     */
    @GetMapping(value = "/admin_category_edit")
    public String editCategory() {
        return "admin/editCategory";
    }

    /**
     * @see #listOrder()
     * 访问../admin_order_list
     * 映射到地址listOrder.html
     */
    @GetMapping(value = "/admin_order_list")
    public String listOrder() {
        return "admin/listOrder";
    }

    /**
     * @see #listProduct()
     * 访问 ../admin_product_list
     * 映射到地址listProduct.html
     */
    @GetMapping(value = "/admin_product_list")
    public String listProduct() {
        return "admin/listProduct";
    }

    /**
     * @see #editProduct()
     * 访问 ../admin_product_edit
     * 映射到地址editProduct.html
     */
    @GetMapping(value = "/admin_product_edit")
    public String editProduct() {
        return "admin/editProduct";
    }

    /**
     * @see #listProductImage()
     * 访问 ../admin_ProductImage_list
     * 跳转到listProductImage.html
     */
    @GetMapping(value = "/admin_productImage_list")
    public String listProductImage() {
        return "admin/listProductImage";
    }

    /**
     * @see #listProperty()
     * 访问 ../admin_property_list
     * 跳转到listProperty.html
     */
    @GetMapping(value = "/admin_property_list")
    public String listProperty() {
        return "admin/listProperty";
    }

    /**
     * @see #editProperty()
     * 访问 ../admin_property_deit
     * 跳转到 admin下到editProperty.html
     */
    @GetMapping(value = "/admin_property_edit")
    public String editProperty() {
        return "admin/editProperty";
    }

    /**
     * @see #editProperty()
     * 访问 ../admin_propertyValue_edit
     * 跳转到 admin 下到editPropertyValue.html
     */
    @GetMapping(value = "/admin_propertyValue_edit")
    public String editPropertyValue() {
        return "admin/editPropertyValue";
    }

    /**
     * @see #listUser()
     * 访问 ../admin_user_list
     * 跳转到 admin下的listUser
     */
    @GetMapping(value = "/admin_user_list")
    public String listUser() {
        return "admin/listUser";
    }
}