package com.huatu.tiku.interview.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.huatu.common.ErrorResult;
import com.huatu.common.exception.BizException;
import com.huatu.tiku.interview.constant.UserConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @Author jbzm
 * @Date Create on 2018/1/23 11:09
 */
@Configuration
public class WebSecurityConfig extends WebMvcConfigurerAdapter {

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/end/login**");

        // 拦截配置
        addInterceptor.addPathPatterns("/end/**");
    }


    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            HttpSession session = request.getSession();
            if (session.getAttribute(UserConstant.SESSION_KEY) != null) {
                return true;
            }
//            // 跳转登录
//            String url = "/wx/end/login";
//            //后台不做跳转   由前台来做路由
//            response.sendRedirect(url);
            throw new BizException(ErrorResult.create(401, "权限不足"));
        }
    }

    /**
     * 声明什么鬼,没他用不了  MultipartFile
     *
     * @return
     */
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding("UTF-8");
        //resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setResolveLazily(true);
        //上传文件大小 50M 50*1024*1024
        resolver.setMaxInMemorySize(40960);
        resolver.setMaxUploadSize(50 * 1024 * 1024);
        return resolver;
    }
}