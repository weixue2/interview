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
import org.springframework.web.bind.annotation.*;
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
    public void template(@RequestParam(required = false) String templateId) {
        templateId = templateId == null ?  "N26DG5jDpHakN8ER7vzT2n2pYy-VLaHDboIFPASTPMk" : templateId ;
        WeChatTemplate wechatTemplate = new WeChatTemplate();
        Map<String, TemplateData> m = Maps.newHashMapWithExpectedSize(16);
        TemplateData first = new TemplateData();
        first.setColor("#000000");
        first.setValue("亲爱的华图各位大佬们");
        m.put("first", first);

        TemplateData keyword1 = new TemplateData();
        keyword1.setColor("#000000");
        keyword1.setValue("韩晶晶");
        m.put("childName", keyword1);

        TemplateData keyword2 = new TemplateData();
        keyword2.setColor("#000000");
        keyword2.setValue("高端面试课第一轮");
        m.put("courseName", keyword2);

        TemplateData keyword3 = new TemplateData();
        keyword3.setColor("#000000");
        keyword3.setValue("59分");
        m.put("score", keyword3);


        TemplateData remark = new TemplateData();
        remark.setColor("#000000");
        remark.setValue("考成这个样子，你可以退学了！");
        m.put("remark", remark);

        wechatTemplate.setData(m);
        wechatTemplate.setTemplate_id(templateId);
        wechatTemplate.setTouser("okdNOuFFsk83SU9bvABn4rJinTvs");
        wechatTemplate.setUrl("http://v.huatu.com");
        weiXinAccessTokenUtil.sendTemplateMessage(  redisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN), wechatTemplate);
    }


}
