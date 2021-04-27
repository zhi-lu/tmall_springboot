package com.luzhi.tmall.interceptor;

import com.luzhi.tmall.pojo.Category;
import com.luzhi.tmall.pojo.OrderItem;
import com.luzhi.tmall.pojo.User;
import com.luzhi.tmall.service.CategoryService;
import com.luzhi.tmall.service.OrderItemService;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/8
 * 生成其他拦截器.跳转首页,遍历数据..
 * 主要配置application和相关session。
 */
@SuppressWarnings("RedundantThrows")
public class OtherInterceptor implements HandlerInterceptor {

    @Resource
    CategoryService categoryService;

    @Resource
    OrderItemService orderItemService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        // 获取相关的session
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");
        int cartTotalItemNumber = 0;
        if (user != null) {
            List<OrderItem> orderItemList = orderItemService.findByUser(user);
            // 遍历订单项
            for (OrderItem orderItem : orderItemList) {
                // 添加商品个数
                cartTotalItemNumber += orderItem.getNumber();
            }
        }
        List<Category> categoryList = categoryService.list();
        // 获取 "../tamll_springboot"地址
        String contextPath = httpServletRequest.getServletContext().getContextPath();
        // 设置application 属性
        httpServletRequest.getServletContext().setAttribute("categories_below_search", categoryList);
        session.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
        httpServletRequest.getServletContext().setAttribute("contextPath", contextPath);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
