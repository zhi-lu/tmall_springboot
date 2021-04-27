package com.luzhi.tmall.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 前端路径导航控制层
 * 创建类似AdminPageController.java功能的前端导航页面...
 * 访问+跳转。。。。。
 */

@Controller
public class ForePageController {

    /**
     * @see #index()
     * 路径初始化跳转
     */
    @GetMapping(value = "/")
    public String index() {
        return "redirect:home";
    }

    /**
     * @see #home()
     * 初始化跳转到主页面
     */
    @GetMapping(value = "/home")
    public String home() {
        return "fore/home";
    }

    /**
     * @see #register()
     * 注册页面
     */
    @GetMapping(value = "/register")
    public String register() {
        return "fore/register";
    }

    /**
     * @see #aliPay()
     * 支付页面
     */
    @GetMapping(value = "/aliPay")
    public String aliPay() {
        return "fore/aliPay";
    }

    /**
     * @see #bought()
     * 购买界面
     */
    @GetMapping(value = "/bought")
    public String bought() {
        return "fore/bought";
    }

    /**
     * @see #buy()
     * 支付界面
     */
    @GetMapping(value = "/buy")
    public String buy() {
        return "fore/buy";
    }

    @GetMapping(value = "/cart")
    public String cart() {
        return "fore/cart";
    }

    /**
     * @see #category()
     * 分类界面;
     */
    @GetMapping(value = "category")
    public String category() {
        return "fore/category";
    }

    /**
     * @see #login()
     * 登录界面
     */
    @GetMapping(value = "/login")
    public String login() {
        return "fore/login";
    }

    /**
     * @see #confirmPay()
     * 确认支付界面
     */
    @GetMapping(value = "/confirmPay")
    public String confirmPay() {
        return "fore/confirmPay";
    }

    /**
     * @see #orderConfirmed()
     * 订单确认界面..
     */
    @GetMapping(value = "/orderConfirmed")
    public String orderConfirmed() {
        return "fore/orderConfirmed";
    }

    /**
     * @see #payed()
     * 支付过后的界面
     */
    @GetMapping(value = "/payed")
    public String payed() {
        return "fore/payed";
    }

    /**
     * @see #product()
     * 商品界面
     */
    @GetMapping(value = "/product")
    public String product() {
        return "fore/product";
    }

    /**
     * @see #registerSuccess()
     * 注册成功的界面
     */
    @GetMapping(value = "/registerSuccess")
    public String registerSuccess() {
        return "fore/registerSuccess";
    }

    /**
     * @see #review()
     * 评论跳转的界面
     */
    @GetMapping(value = "/review")
    public String review() {
        return "fore/review";
    }

    /**
     * @see #search()
     * 搜索框搜索后产生的界面...
     */
    @GetMapping(value = "/search")
    public String search() {
        return "fore/search";
    }

    /**
     * @see #foreLogout(HttpSession)
     * 退出登录.为什么不在 foreRestController解析foreLogout
     * 因为restController需要相关的json返回。
     */
    @GetMapping(value = "/foreLogout")
    public String foreLogout(@SuppressWarnings("unused") HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        // 如果当前已经登录..
        if (subject.isAuthenticated()) {
            // 退出已经登录..
            subject.logout();
        }
        return "redirect:home";
    }
}
