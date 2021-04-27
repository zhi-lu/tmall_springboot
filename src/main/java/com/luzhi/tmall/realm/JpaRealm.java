package com.luzhi.tmall.realm;

import com.luzhi.tmall.pojo.User;
import com.luzhi.tmall.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;

/**
 * @author apple
 * @version jdk1.8
 * // TODO : 2021/3/21
 * 生成Jpa域,对用户进行加密
 * 使用SpringBoot进行shiro权限配置,不能使用shiro.ini而是去实现相关的类
 * // 详细的使用请看:
 * @see AuthorizingRealm#doGetAuthorizationInfo(PrincipalCollection)
 * @see AuthorizingRealm#doGetAuthenticationInfo(AuthenticationToken)
 */
public class JpaRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return new SimpleAuthorizationInfo();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String userName = authenticationToken.getPrincipal().toString();
        User user = userService.getByName(userName);
        String passwordInDateBase = user.getPassword();
        String salt = user.getSalt();
        return new SimpleAuthenticationInfo(userName, passwordInDateBase, ByteSource.Util.bytes(salt),
                this.getName());
    }
}
