package com.huatu.tiku.interview.task;

import com.alibaba.fastjson.JSON;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.AccessToken;
import com.huatu.tiku.interview.entity.dto.ReadingTemp;
import com.huatu.tiku.interview.entity.po.MorningReading;
import com.huatu.tiku.interview.repository.MorningReadingRepository;
import com.huatu.tiku.interview.service.MorningReadingService;
import com.huatu.tiku.interview.util.WeiXinAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
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
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    WeiXinAccessTokenUtil weiXinAccessTokenUtil;

    @Autowired
    ServletContext servletContext;

    @Autowired
    HttpSession httpSession;

    @Autowired
    MorningReadingRepository readingRepository;

    // 第三方用户唯一凭证
//    public static AccessToken accessToken = null;

    public static String accessToken = "";
    //TODO 分布式锁保证只有一台机器执行

    @Scheduled(fixedDelay = 2 * 3600 * 1000)
    public void getReading(){
        List<MorningReading> all = readingRepository.findAll();
        if(!all.isEmpty()){
            List<ReadingTemp> rts = new ArrayList<>();
            for (MorningReading mr:all){
                rts.add(new ReadingTemp(mr.getId(),mr.getPushTime()));
            }
            String json = JSON.toJSONString(rts);
            stringRedisTemplate.opsForValue().set("readings", json);
//            redisTemplate.opsForValue().set("readings",rts);
//            httpSession.setAttribute("test1996","tesst1996");
            redisTemplate.opsForValue().set("test1996","tesst1996");
        }
    }

    //7200秒执行一次
    @Scheduled(fixedDelay = 2 * 3600 * 1000)
    public void getToken() {
        String token = stringRedisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN);
        if(StringUtils.isEmpty(token)){
            log.info("getToken");
            accessToken = weiXinAccessTokenUtil.getAccessToken();
            //accessToken 不可能为空 不用判断
            if (accessToken != null) {
                stringRedisTemplate.opsForValue().set(WeChatUrlConstant.ACCESS_TOKEN, accessToken);
                stringRedisTemplate.expire(WeChatUrlConstant.ACCESS_TOKEN,7100, TimeUnit.SECONDS);
                log.info("获取成功，accessToken:" + accessToken);
            } else {
                log.error("获取token失败");
            }
        }else{
            log.info("已有accessToken");
        }

    }
}
//7200秒执行一次
//    @Scheduled(fixedDelay = 2 * 3600 * 1000)
//    public void getToken() {
//        log.info("getToken");
//        accessToken = weiXinAccessTokenUtil.getWeiXinAccessToken();
//        //accessToken 不可能为空 不用判断
//        if (accessToken.getAccess_token() != null) {
//            redisTemplate.opsForValue().set(WeChatUrlConstant.ACCESS_TOKEN, accessToken.getAccess_token());
//            log.info("获取成功，accessToken:" + accessToken.getAccess_token());
//        } else {
//            log.error("获取token失败");
//        }
//    }