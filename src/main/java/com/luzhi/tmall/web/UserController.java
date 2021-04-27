package com.luzhi.tmall.web;

import com.luzhi.tmall.pojo.User;
import com.luzhi.tmall.service.UserService;
import com.luzhi.tmall.util.Page4Navigator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 用户控制层的实现.
 */
@RestController
public class UserController {

    @Resource
    UserService userService;

    /**
     * @see #list(int, int)
     * 查找相关的用户数据
     */
    @GetMapping("/users")
    public Page4Navigator<User> list(@RequestParam(value = "start", defaultValue = "0") int start,
                                     @RequestParam(value = "size", defaultValue = "5") int size) throws Exception{

        start = Math.max(0, start);
        return userService.list(start, size, 5);
    }
}
