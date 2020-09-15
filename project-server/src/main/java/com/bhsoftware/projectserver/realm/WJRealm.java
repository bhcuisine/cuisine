package com.bhsoftware.projectserver.realm;

import com.bhsoftware.projectserver.entity.User;
import com.bhsoftware.projectserver.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Set;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/7
 */

public class WJRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    //重写获取授权信息的方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        //把权限放入授权信息中
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        return simpleAuthorizationInfo;
    }

    //获取认证信息，即根据token中的用户名从数据库中获取密码、盐等并返回
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //post请求时先进行认证，再请求
        String userName = token.getPrincipal().toString();
        if (userName == null){
            return null;
        }
        User user = userService.getByName(userName);
        if (ObjectUtils.isEmpty(user)){
            throw new UnknownAccountException();
        }
        String passwordInDB = user.getPassword();
        String salt = user.getSalt();
        SimpleAuthenticationInfo authorizationInfo =
                new SimpleAuthenticationInfo(userName, passwordInDB, ByteSource.Util.bytes(salt), getName());
        return authorizationInfo;
    }

}
