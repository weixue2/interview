package com.huatu.tiku.interview.spring.conf.web;

import com.huatu.tiku.springboot.users.support.TokenMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 千万不要在里面定义bean,会导致事务失效。。。。原因大概是代理类出错
 *
 * @author hanchao
 * @date 2017/8/25 17:40
 */
@Configuration
@ServletComponentScan("com.huatu")//servlet扫描配置
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TokenMethodArgumentResolver tokenMethodArgumentResolver;

    /**
     * 为了方便定义消息处理器的顺序，理论上处理器越少，处理中，需要循环遍历判断的次数也越少
     */
    @Autowired
    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private StringHttpMessageConverter stringHttpMessageConverter;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(tokenMethodArgumentResolver);//用户session参数装配
        super.addArgumentResolvers(argumentResolvers);
    }

//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "DELETE", "PUT")
//                .exposedHeaders("Set-Cookie")
//                .maxAge(3600);
//    }

    /**
     * springBoot中的messageconverters构建-》httpmessageconverters->webmvcconfig(configures)->autowebmvcConfig
     * httpmessageconverters会影响feign中使用到的converter，所以从这里修改
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        //jackson放在第一位，因为在默认没有声明accept以及handler produce的时候，会根据处理器去判断
        converters.add(mappingJackson2HttpMessageConverter);
        converters.add(stringHttpMessageConverter);
        converters.add(new ByteArrayHttpMessageConverter());
    }


}
