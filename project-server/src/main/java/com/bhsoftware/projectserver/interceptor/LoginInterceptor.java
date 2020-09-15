package com.bhsoftware.projectserver.interceptor;

import com.bhsoftware.projectserver.entity.User;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/8
 */
public class LoginInterceptor implements HandlerInterceptor {

//    //拦截器
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
//        //放行options请求，否则前端不能带自定义header信息，导致sessionID变化，shrio验证失败
//        if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())){
//            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
//            return true;
//        }
//        Subject subject = SecurityUtils.getSubject();
//        System.out.println(subject.isRemembered());
//        System.out.println(subject.isAuthenticated());
//        //if (!subject.isAuthenticated()){
//        if (!subject.isAuthenticated()&& !subject.isRemembered()){
//            return false;
//        }
//        return true;
//    }


    //拦截器
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession();
//        String contextPath = session.getServletContext().getContextPath();
//        String[] requireAuthPages = new String[]{
//                "index",
//        };
//        String uri = request.getRequestURI();
//        uri = StringUtils.remove(uri, contextPath + '/');
//        String page = uri;
//
//        if (beginWith(page,requireAuthPages)) {
//            User user = (User) session.getAttribute("user");
//            if (user == null){
//                response.sendRedirect("login");
//                return false;
//            }
//        }
//        return true;
//    }

//    private boolean beginWith(String page, String[] requiredAuthPages) {
//        boolean result = false;
//        for (String requiredAuthPage : requiredAuthPages) {
//            if (StringUtils.startsWith(page, requiredAuthPage)) {
//                result = true;
//                break;
//            }
//        }
//        return result;
//    }
}
