package com.bhsoftware.projectserver.controller;

import com.bhsoftware.projectserver.entity.User;
import com.bhsoftware.projectserver.result.Result;
import com.bhsoftware.projectserver.result.ResultFactory;
import com.bhsoftware.projectserver.service.UserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;


import java.util.Objects;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/7/31
 */
@Controller      //RestController  == Controller+ ResponseBody
public class LoginController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @ResponseBody
    @GetMapping(value = "/api/authentication")
    public String authentication(){
        return "身份认证成功。";
    }

    @CrossOrigin
    @ResponseBody
    @GetMapping("/api/logout")
    public Result logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultFactory.buildSuccessResult("退出成功！");
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/api/register")
    public Result register(@RequestBody User user) {

        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);

        // 判断用户名不能为空
        if (username.equals("")) {
            String message = "用户名不能为空！";
            return ResultFactory.buildFailResult(message);
        }
        boolean exist = userService.isExist(username);
        if (exist) {
            String message = "该用户名已被占用！";
            return ResultFactory.buildFailResult(message);
        }

        //判断密码
        if (user.getPassword().equals("")){
            String message = "密码不能为空!";
            return ResultFactory.buildFailResult(message);
        }

        //生成盐，默认为16位
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();

        //设置hash迭代次数
        int times = 2;

        //得到hash运算值
        String encodePassword = new SimpleHash("md5", password, salt, times).toString();

        //存储用户信息--salt及hash运算值
        user.setSalt(salt);
        user.setPassword(encodePassword);
        userService.add(user);
        return ResultFactory.buildSuccessResult(user);
    }

    @CrossOrigin
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {

        String username = requestUser.getUsername();

        //判断用户名不能为空
        if (username.equals("")){
            String message = "用户名不能为空。";
            return ResultFactory.buildFailResult(message);
        }
        //===============================
        //转义html标签，防止xss攻击
         username = HtmlUtils.htmlEscape(username);
//        if (!Objects.equals("admin", username) || !Objects.equals("123456", requestUser.getPassword())) {
//            String message = "账号密码错误";
//            System.out.println(message);
//            return new Result(400);
//        } else {
//            return new Result(200);
//        }

//        User user = userService.get(username, requestUser.getPassword());
//        if (null == user) {
//            return new Result(400);
//        } else {
//            return new Result(200);
//        }

        //===============================
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        usernamePasswordToken.setRememberMe(true);
        try {
            subject.login(usernamePasswordToken);
            return ResultFactory.buildSuccessResult(username);
        } catch (AuthenticationException e) {
            String message = "账号密码错误！";
            return ResultFactory.buildFailResult(message);
        }
    }
}
