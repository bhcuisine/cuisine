package com.bh.bhcuisine.controller;

import com.bh.bhcuisine.config.ShiroUtil;
import com.bh.bhcuisine.entity.User;
import com.bh.bhcuisine.result.Result;
import com.bh.bhcuisine.result.ResultFactory;
import com.bh.bhcuisine.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param username 用户名
     * @param password 密码
     *                 @RequestParam String username,@RequestParam String password
     * @return
     */
    @ApiOperation(value = "用户登录" ,  notes="用户登录")
    @PostMapping(value = "/api/login")
    public Result login(@RequestBody User reuser
            ) {
        User user = userService.getByUsername(reuser.getUsername());
        if(user!=null){
            return ResultFactory.buildFailResult("用户名已经注册");
        }else{
            //提交登录
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isAuthenticated()) {
                UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), reuser.getPassword());
                token.setRememberMe(true);
                subject.login(token);
                return ResultFactory.buildSuccessResult(user);
            }
            else {
                return ResultFactory.buildFailResult("失败");
            }
        }
    }

    /**
     * 用户修改密码
     * @param newPassword 新密码
     * @return
     */
    @ApiOperation(value = "用户修改密码" ,  notes="用户修改密码")
    @PostMapping(value = "/api/resetPassword")
    public Result resetPassword(
            @RequestBody User reuser
//            @RequestParam String newPassword
        ){
//        System.out.println(reuser.getPassword());
        User user=(User)SecurityUtils.getSubject().getSession().getAttribute("user");
        Integer id=user.getId();
        System.out.println("当前用户"+user);
        String password= ShiroUtil.sha256(reuser.getPassword(),user.getSalt());
        userService.updatePassWord(password,id);
        return ResultFactory.buildSuccessResult(user);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    @PostMapping("/api/loginout")
    @ResponseBody
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultFactory.buildSuccessResult("退出成功");
    }

}
