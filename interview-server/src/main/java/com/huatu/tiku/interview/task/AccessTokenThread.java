package com.huatu.tiku.interview.task;

import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.AccessToken;
import com.huatu.tiku.interview.util.WeiXinAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    ServletContext servletContext;

    public static String accessToken = "";
    //TODO 分布式锁保证只有一台机器执行

    /**
     * token存入redis
     */
    @Scheduled(fixedDelay = 2 * 3600 * 1000 - 100)
    public void getToken() {
        log.info("getToken");
        accessToken = WeiXinAccessTokenUtil.getAccessToken();
        if (StringUtils.isNotEmpty(accessToken)) {
            redisTemplate.opsForValue().set(WeChatUrlConstant.ACCESS_TOKEN_KEY, accessToken);
            log.info("获取成功,accessToken:" + accessToken);
        } else {
            log.info("获取token失败");
        }
    }

}
