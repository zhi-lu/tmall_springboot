package com.luzhi.tmall.web;

import com.luzhi.tmall.pojo.Category;
import com.luzhi.tmall.service.CategoryService;
import com.luzhi.tmall.util.ImageUtil;
import com.luzhi.tmall.util.Page4Navigator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21l
 * // 生成分类控制页面,使用Restful风格(我不会在写一遍了)
 * 此控制类使用@RestController将json数据抛给浏览器...
 */

@RestController
public class CategoryController {

	@Resource
	CategoryService categoryService;

	/**
	 * @see #list(int, int)
	 * 设定start(其实位置),size(页面数据的多少)通过@RequestParam() 设定
	 */
	@GetMapping("/categories")
	public Page4Navigator<Category> list(@RequestParam(value = "start", defaultValue = "0") int start,
										 @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {

		start = Math.max(0, start);
		//5表示导航分页最多有5个，像 [1,2,3,4,5] 这样
		return categoryService.list(start, size, 5);
	}

	/**
	 * @see #add(Category, MultipartFile, HttpServletRequest)
	 * 添加对象
	 * 提交属性对应的action methods is "POST"
	 */
	@PostMapping("/categories")
	public Object add(Category bean, MultipartFile image, HttpServletRequest request) throws Exception {

		categoryService.add(bean);
		saveOrUpdateImageFile(bean, image, request);
		return bean;
	}

	/**
	 * @see #saveOrUpdateImageFile(Category, MultipartFile, HttpServletRequest)
	 * 保存图片到指定位置.
	 */
	public void saveOrUpdateImageFile(Category bean, MultipartFile image, HttpServletRequest request)
			throws IOException {
		File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, bean.getId() + ".jpg");
		if (!file.getParentFile().exists()) {
			//noinspection ResultOfMethodCallIgnored
			file.getParentFile().mkdirs();
		}

		image.transferTo(file);
		BufferedImage img = ImageUtil.change2jpg(file);
		assert img != null;
		ImageIO.write(img, "jpg", file);
	}

	/**
	 * @see #delete(int, HttpServletRequest)
	 * 删除对象(包括对应的图片)
	 * 删除对应的 action method is "DELETE" through javascript framework Jquery transfer;
	 * 返回对此不做任何响应..
	 */
	@DeleteMapping("/categories/{id}")
	public String delete(@PathVariable("id") int id, HttpServletRequest request) {
		categoryService.delete(id);
		File imageFolder = new File(request.getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder, id + ".jpg");
		//noinspection ResultOfMethodCallIgnored
		file.delete();
		return null;
	}

	/**
	 * @see #get(int)
	 * 通过id获取对象
	 * 过去对应的 action method is "GET"
	 */
	@GetMapping("/categories/{id}")
	public Category get(@PathVariable("id") int id) throws Exception {
		return categoryService.get(id);
	}

	/**
	 * @see #update(Category, MultipartFile, HttpServletRequest)
	 * 通过id 更新对象
	 * 对应 action method is "UPDATE"
	 */
	@PutMapping("/categories/{id}")
	public Object update(Category bean, MultipartFile image, HttpServletRequest request) throws Exception {
		String name = request.getParameter("name");
		bean.setName(name);
		categoryService.update(bean);

		if (image != null) {
			saveOrUpdateImageFile(bean, image, request);
		}
		return bean;
	}
}

