package com.luzhi.tmall.interceptor;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/11
 * 生成一个拦截器.实现HandleInterceptor接口
 * 具体实现:
 * @see HandlerInterceptor#preHandle(HttpServletRequest, HttpServletResponse, Object)
 * 现在具体使用Shiro进行登录{@link org.apache.shiro.SecurityUtils} getObject通过当前Session 获取是否登录
 * (User)对象...
 */
@SuppressWarnings("RedundantThrows")
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 判断是否有需要拦截的地址.然后在判断是否登录.没有登录则转到登录界面...
     * 具体实现:
     *
     * @return boolean
     * @see #preHandle(HttpServletRequest, HttpServletResponse, Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 获取当前的session
        HttpSession session = httpServletRequest.getSession();
        // 获取当前的访问地址(头)
        String context = session.getServletContext().getContextPath();
        // 罗列出相关需要登录才能访问的界面或功能..
        String[] requiredAuthPages = new String[]{
                "buy",
                "aliPay",
                "payed",
                "cart",
                "bought",
                "confirmPay",
                "orderConfirmed",

                "foreBuyOne",
                "foreBuy",
                "foreAddCart",
                "foreCart",
                "foreCreateOrderItem",
                "foreDeleteOrderItem",
                "foreChangeOrderItem",
                "forePayed",
                "foreBought",
                "foreConfirmPay",
                "foreOrderConfirmed",
                "foreDeleteOrder",
                "foreReview",
                "foreDoReview"
        };
        String uri = httpServletRequest.getRequestURI();
        // 除去 "../tamll_springboot" 保留"具体"的地址或功能
        uri = StringUtils.remove(uri, context + "/");
        String page = uri;
        if (ergodicWith(page, requiredAuthPages)) {
            // 获取当前"用户"的状态....
            Subject subject = SecurityUtils.getSubject();
            // 查看当前是否登录..(认证过后的User 登录...)
            if (!subject.isAuthenticated()) {
                httpServletResponse.sendRedirect("login");
            }
        }
        return true;
    }

    /**
     * @see #ergodicWith(String, String[])
     * 具体遍历比较,访问的地址是否在需要登录地址的数组中..........
     * 具体请看:比较字符串
     * @see StringUtils#startsWith(String, String)
     */
    private boolean ergodicWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String accessAuthPage : requiredAuthPages) {
            if (StringUtils.startsWith(page, accessAuthPage)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
}
