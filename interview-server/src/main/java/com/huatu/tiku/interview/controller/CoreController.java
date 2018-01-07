package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.AccessToken;
import com.huatu.tiku.interview.service.CoreService;
import com.huatu.tiku.interview.task.AccessTokenThread;
import com.huatu.tiku.interview.util.SignUtil;
import com.huatu.tiku.interview.util.WeiXinAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhouwei
 * @Description:
 * @create 2018-01-04 上午11:53
 **/
@RestController
@Slf4j
public class CoreController {
    @Autowired
    private CoreService coreService;
    @Autowired
    StringRedisTemplate redisTemplate;


    @Autowired
    WeiXinAccessTokenUtil weiXinAccessTokenUtil;


   // @PostMapping(value = "", produces = "application/xml; charset=UTF-8")
    @PostMapping(value = "")
    public Object post(HttpServletRequest req) {
        // 调用核心业务类接收消息、处理消息跟推送消息
        log.info("--------------core-------------------");
        String respMessage = coreService.processRequest(req);
        return respMessage;
    }

    @GetMapping("token")
    public String getToken() {
        log.info("getToken");
        return redisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN);
    }

    @PutMapping("token")
    public Object getAndSetToken() {
        log.info("getAndSetToken");
        AccessToken accessToken = weiXinAccessTokenUtil.getWeiXinAccessToken();

        if (accessToken.getAccess_token() != null) {
            redisTemplate.opsForValue().set(WeChatUrlConstant.ACCESS_TOKEN, accessToken.getAccess_token());
            log.info("获取成功，accessToken:" + accessToken.getAccess_token());
        } else {
            log.error("获取token失败");
        }
        return accessToken;
    }
}
