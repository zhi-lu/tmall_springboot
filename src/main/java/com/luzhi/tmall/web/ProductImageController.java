package com.luzhi.tmall.web;

import com.luzhi.tmall.pojo.Product;
import com.luzhi.tmall.pojo.ProductImage;
import com.luzhi.tmall.service.CategoryService;
import com.luzhi.tmall.service.ProductImageService;
import com.luzhi.tmall.service.ProductService;
import com.luzhi.tmall.util.ImageUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author apple
 * @version jdk8
 * // TODO:2021/3/21
 * // 产品图片控制,产品图片没有edit update. 要么找,要么删除,要么添加
 * 因为有单一图片和详情图片所以要不同去考虑,是否要大中小的图片.
 * crud王八拳一打,我脑门也不秃了。此时我想找个老婆~~~~~~~~~~~~~~~~~~~.
 */
@RestController
public class ProductImageController {

    @Resource
    CategoryService categoryService;

    @Resource
    ProductService productService;

    @Resource
    ProductImageService productImageService;

    /**
     * @see #list(String, int)
     * 查找图片对象. @RequestParam("type") 接受前端传来的type值
     */
    @GetMapping("/products/{pid}/productImages")
    public List<ProductImage> list(@RequestParam("type") String type, @PathVariable("pid") int pid) throws Exception {

        Product product = productService.get(pid);
        if (ProductImageService.TYPE_SINGLE.equals(type)) {
            return productImageService.listSingleProductImage(product);
        } else if (ProductImageService.TYPE_DETAIL.equals(type)) {
            return productImageService.listDetailProductImages(product);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * @see #add(int, String, MultipartFile, HttpServletRequest)
     * 添加图片
     */
    @SuppressWarnings({"ResultOfMethodCallIgnored", "DuplicatedCode"})
    @PostMapping("/productImages")
    public Object add(@RequestParam("pid") int pid, @RequestParam("type") String type,
                      MultipartFile image, HttpServletRequest request) throws Exception {
        ProductImage bean = new ProductImage();
        Product product = productService.get(pid);
        bean.setProduct(product);
        bean.setType(type);
        productImageService.add(bean);
        String folder = "img/";
        if (ProductImageService.TYPE_SINGLE.equals(bean.getType())) {
            folder += "productSingle";
        } else {
            folder += "productDetail";
        }
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder, bean.getId() + ".jpg");
        String fileName = file.getName();
        // 判断文件的父亲目录是否存在
        if (!file.getParentFile().exists()) {
            // 创建该目录
            file.getParentFile().mkdirs();
        }
        try {
            image.transferTo(file);
            BufferedImage imageOne = ImageUtil.change2jpg(file);
            // 设置断言判断 imageOne不为空
            assert imageOne != null;
            ImageIO.write(imageOne, "jpg", file);
        } catch (IOException e) {
            System.out.println("打印异常原因:" + e.getMessage());
            System.out.println("打印异常回溯");
            e.printStackTrace();
        }
        // 在img创建productSingle_small和 productSingle_middle文件夹,保存小,中,原图片
        if (ProductImageService.TYPE_SINGLE.equals(bean.getType())) {
            String imageFolderSmall = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolderMiddle = request.getServletContext().getRealPath("img/productSingle_middle");
            File imageSmall = new File(imageFolderSmall, fileName);
            File imageMiddle = new File(imageFolderMiddle, fileName);
            imageSmall.getParentFile().mkdirs();
            imageMiddle.getParentFile().mkdirs();
            ImageUtil.resizeImage(file, 56, 56, imageSmall);
            ImageUtil.resizeImage(file, 217, 170, imageMiddle);
        }
        return bean;
    }

    /**
     * @see #delete(int, HttpServletRequest)
     * 删除照片.包括下面的目录
     */
    @SuppressWarnings({"DuplicatedCode", "ResultOfMethodCallIgnored"})
    @DeleteMapping("/productImages/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) throws Exception {

        ProductImage bean = productImageService.get(id);
        productImageService.delete(id);

        String folder = "img/";
        if (ProductImageService.TYPE_SINGLE.equals(bean.getType())) {
            folder += "productSingle";
        } else {
            folder += "productDetail";
        }
        File imageFolder = new File(request.getServletContext().getRealPath(folder));
        File file = new File(imageFolder, bean.getId() + ".jpg");
        String fileName = file.getName();
        file.delete();
        // 删除原来的图片的同时,也删除保存在img下的productSingle_middle 和 productSingle_small 的 中, 小 图片。
        if (ProductImageService.TYPE_SINGLE.equals(bean.getType())) {
            String imageFolderSmall = request.getServletContext().getRealPath("img/productSingle_small");
            String imageFolderMiddle = request.getServletContext().getRealPath("img/productSingle_middle");
            File imageSmall = new File(imageFolderSmall, fileName);
            File imageMiddle = new File(imageFolderMiddle, fileName);
            imageSmall.delete();
            imageMiddle.delete();
        }
        return null;
    }
}
