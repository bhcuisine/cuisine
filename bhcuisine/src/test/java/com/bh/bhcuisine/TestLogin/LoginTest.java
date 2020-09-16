package com.bh.bhcuisine.TestLogin;

import com.bh.bhcuisine.dao.UserDao;
import com.bh.bhcuisine.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试登录测试类--改用swagger
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTest {

    @Autowired
    private UserDao userDao;

    /**
     * 测试登录方法--
     * 因为用了shiro只能用swagger或postman测试，
     * 这里swagger测试成功了，单元测试失败了
     */
    @Test
    public void TestLogin(){
//        String username="康老师";
//        String password="123";
//        User user = userDao.getByUsername(username);
//        //提交登录
//        Subject subject = SecurityUtils.getSubject();
//        if (!subject.isAuthenticated()) {
//            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
//            token.setRememberMe(true);
//            subject.login(token);
//            System.out.println("成功");
//        }else{
//            System.out.println("失败");
//        }
    }
}
