package com.bhsoftware.projectserver.config;

import com.bhsoftware.projectserver.interceptor.LoginInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description:
 * @Author: YTF
 * @Date: 2020/8/6
 */
@SpringBootConfiguration
public class MyWebConfigurer implements WebMvcConfigurer {

    //图片上传
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/api/file/**").addResourceLocations("file:"+"d:/vue/img1/");
    }

    @Bean
    public LoginInterceptor getLoginIntercepter(){
         return new LoginInterceptor();
    }

    //跨域处理
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        //所有请求都允许跨域，使用此配置方式则不能在interceptor中配置header
        registry.addMapping("/**")
                .allowCredentials(true)  //允许跨域使用cookies，allowedOrigins()不能使用通配符*
                .allowedOrigins("http://localhost:8080")
                //.allowedOrigins("http://localhost:8081")
                .allowedMethods("POST","GET","PUT","OPTIONS","DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    //配置后端拦截器-----暂时注掉    8月10日
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(getLoginIntercepter())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/index.html");

        registry.addInterceptor(getLoginIntercepter())
                .addPathPatterns("/**")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/logout");
    }
}
