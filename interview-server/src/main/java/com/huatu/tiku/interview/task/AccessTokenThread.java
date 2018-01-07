package com.huatu.tiku.interview.task;

import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.AccessToken;
import com.huatu.tiku.interview.util.WeiXinUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-04 下午2:00
 **/
@Component
@Slf4j
public class AccessTokenThread {

    // 第三方用户唯一凭证
    public static AccessToken accessToken =   new AccessToken("5_f2LWdG3R96BetLeYP8pNm8mG3eO6qnoqbAvDZt1VssabSHi9G8VS4rKH4uH31vc7TT8hIIB8KFPp-uctGVLFodmJlxg6mecWqNBuJz-sH01PFZXvrquUxc_XwARNuOQCkSPGuQtO84-QkKUVHAMiACAXQC",100);
//TODO 存缓存
  //  @Scheduled(fixedDelay = 2*3600*1000)
    //7200秒执行一次
    public void getToken(){
        accessToken= WeiXinUtil.getAccessToken(WeChatUrlConstant.APP_ID,WeChatUrlConstant.ACCESS_SECRETE);
        if(null!=accessToken){
            log.info("获取成功，accessToken:"+accessToken.getAccess_token());
        }else {
            log.error("获取token失败");
        }
    }
}
