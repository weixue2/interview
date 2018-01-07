package com.huatu.tiku.interview.controller;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.huatu.common.ErrorResult;
import com.huatu.common.exception.BizException;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.*;
import com.huatu.tiku.interview.util.WeiXinAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-03 上午11:11
 **/
@Slf4j
@RestController
@RequestMapping(value = "/api",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TestController {

    @Autowired
    WeiXinAccessTokenUtil weiXinAccessTokenUtil;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("template")
    public void template() {
        log.info("template get");

        WeChatTemplate wechatTemplate = new WeChatTemplate();
        Map<String, TemplateData> m = Maps.newHashMapWithExpectedSize(16);
        TemplateData first = new TemplateData();
        first.setColor("#000000");
        first.setValue("您的户外旅行活动订单已经支付完成，可在我的个人中心中查看。");
        m.put("first", first);

        TemplateData keyword1 = new TemplateData();
        keyword1.setColor("#000000");
        keyword1.setValue("1.2发现尼泊尔—人文与自然的旅行圣地 ");
        m.put("keyword1", keyword1);

        TemplateData keyword2 = new TemplateData();
        keyword2.setColor("#000000");
        keyword2.setValue("5000元");
        m.put("keyword2", keyword2);

        TemplateData keyword3 = new TemplateData();
        keyword3.setColor("#000000");
        keyword3.setValue("2017.1.2");
        m.put("keyword3", keyword3);

        TemplateData keyword4 = new TemplateData();
        keyword4.setColor("#000000");
        keyword4.setValue("5");
        m.put("keyword4", keyword4);

        TemplateData remark = new TemplateData();
        remark.setColor("#000000");
        remark.setValue("请届时携带好身份证件准时到达集合地点，若临时退改将产生相应损失，敬请谅解,谢谢！");
        m.put("remark", remark);

        wechatTemplate.setData(m);
        wechatTemplate.setTemplate_id("1TNrzYDSfkLO2Z0chx_JeEwTFn-znJOpAskYS4MNDK8");
        wechatTemplate.setTouser("");
        wechatTemplate.setUrl("http://v.huatu.com");
        weiXinAccessTokenUtil.sendTemplateMessage(  redisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN), wechatTemplate);
    }


}
