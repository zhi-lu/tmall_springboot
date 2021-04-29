package com.luzhi.tmall.web;

import com.luzhi.tmall.pojo.AdminUser;
import com.luzhi.tmall.service.AdminUserService;
import com.luzhi.tmall.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/4/15
 * 管理员登录控制层.这里我还是使用{@link RestController} 前端是以json提交的数据.
 * 不能使用Controller。
 */

@RestController
public class AdminUserController {

    @Resource
    AdminUserService adminUserService;

    /**
     * @param userParam 从前端获取当前的AdminUser对象
     * @param session   获取当前的session
     * @see #loginAdmin(AdminUser, HttpSession)
     * 具体查找{@link AdminUserService#getAdminUser(String, String)}
     */
    @PostMapping("/loginAdmin")
    public Object loginAdmin(@RequestBody AdminUser userParam, HttpSession session) {
        String name = userParam.getName();
        // 先进行相关的转义
        name = HtmlUtils.htmlEscape(name);
        AdminUser adminUser = adminUserService.getAdminUser(name, userParam.getPassword());
        // 对象不为空则登录成功
        if (null != adminUser) {
            session.setAttribute("adminUser", adminUser);
            return Result.success();
        } else {
            return Result.fail("管理员账号密码不存在- m（_ _）m");
        }
    }
}
