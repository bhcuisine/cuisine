package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.config.ShiroUtil;
import com.bh.bhcuisine.entity.User;
import com.bh.bhcuisine.result.Result;
import com.bh.bhcuisine.result.ResultFactory;
import com.bh.bhcuisine.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

/**
 * 登录controller
 */
@RestController
public class LoginController {

    /**
     * 注入用户service层
     */
    @Autowired
    private UserService userService;


    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     * @RequestParam String username,@RequestParam String password
     */
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping(value = "/api/login")
    public Result login(@RequestBody User requestUser) {
        String username = requestUser.getUsername().replaceAll("　| ", "");//去除全角和半角空格
        username = HtmlUtils.htmlEscape(username);

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        usernamePasswordToken.setRememberMe(true);
        try {
            subject.login(usernamePasswordToken);
            User user = userService.getByUsername(username);//根据用户名得到用户实体
            if(user.getEnabled()==0){
                return ResultFactory.buildFailResult("用户已经被禁用");
            }else{
                return ResultFactory.buildSuccessResult(user);
            }
        } catch (IncorrectCredentialsException e) {
            return ResultFactory.buildFailResult("密码错误");
        } catch (UnknownAccountException e) {
            return ResultFactory.buildFailResult("账号不存在");
        }
    }

    /**
     * 用户修改密码
     *
     * @param newPassword 新密码
     * @return
     */
    @ApiOperation(value = "用户修改密码", notes = "用户修改密码")
    @PostMapping(value = "/api/resetPassword")
    public Result resetPassword(
            @RequestBody User reuser
    ) {
        String username = reuser.getUsername();
        User user = userService.getByUsername(username);
        Integer id = user.getId();
        String password = ShiroUtil.sha256(reuser.getPassword(), user.getSalt());
        userService.updatePassWord(password, id);
        return ResultFactory.buildSuccessResult(user);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    @PostMapping("/api/loginout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultFactory.buildSuccessResult("退出成功");
    }

}
