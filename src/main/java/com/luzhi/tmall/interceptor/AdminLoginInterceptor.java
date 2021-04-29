package com.luzhi.tmall.interceptor;

import com.luzhi.tmall.pojo.AdminUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/16
 * 生成后端页面登录拦截器..
 */
@SuppressWarnings("RedundantThrows")
public class AdminLoginInterceptor implements HandlerInterceptor {

    /**
     * 判断是否有需要拦截的地址.然后在判断是否登录.没有登录则转到登录界面...
     * 具体实现:
     *
     * @return boolean
     * @see #preHandle(HttpServletRequest, HttpServletResponse, Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 获取当前的session.
        HttpSession session = httpServletRequest.getSession();
        // 获取当访问的地址
        String context = session.getServletContext().getContextPath();
        String[] requirePath = new String[]{

                "admin_category_list",
                "admin_category_edit",
                "admin_order_list",
                "admin_product_list",
                "admin_product_edit",
                "admin_productImage_list",
                "admin_property_list",
                "admin_property_edit",
                "admin_propertyValue_edit",
                "admin_user_list",
        };
        String uri = httpServletRequest.getRequestURI();
        // 除去../tmall_springBoot/ 这部分Url
        uri = StringUtils.remove(uri, context + "/");
        String page = uri;
        // 捕获当前已经解析的地址是否在后端的地址中.
        if (LoginInterceptor.ergodicWith(page,requirePath)){
            AdminUser adminUser = (AdminUser) session.getAttribute("adminUser");
            // 判断现在 session的adminUser对象是否为空
            if (null == adminUser){
                httpServletResponse.sendRedirect("adminLogin");
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
}
