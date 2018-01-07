package com.huatu.tiku.interview.task;

import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.AccessToken;
import com.huatu.tiku.interview.util.WeiXinAccessTokenUtil;
import com.huatu.tiku.interview.util.WeiXinUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhouwei
 * @Description: 获取access_token
 * @create 2018-01-04 下午2:00
 **/
@Component
@Slf4j
public class AccessTokenThread {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    WeiXinAccessTokenUtil weiXinAccessTokenUtil;

    // 第三方用户唯一凭证
    public static AccessToken accessToken = null;
    //TODO 分布式锁保证只有一台机器执行

    //7200秒执行一次
    @Scheduled(fixedDelay = 2 * 3600 * 1000)
    public void getToken() {
        log.info("getToken");
        accessToken = weiXinAccessTokenUtil.getWeiXinAccessToken();
          //accessToken 不可能为空 不用判断
        if (accessToken.getAccess_token() != null) {
            redisTemplate.opsForValue().set(WeChatUrlConstant.ACCESS_TOKEN, accessToken.getAccess_token());
            log.info("获取成功，accessToken:" + accessToken.getAccess_token());
        } else {
            log.error("获取token失败");
        }
    }
}
