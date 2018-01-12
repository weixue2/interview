package com.huatu.tiku.interview.service.impl;

import com.alibaba.fastjson.JSON;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.result.PhpResult;
import com.huatu.tiku.interview.service.MobileService;
import com.huatu.tiku.interview.util.Crypt3Des;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/10 21:55
 * @Modefied By:
 */
@Service
public class MobilServiceImpl implements MobileService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String checkPHP(String mobile) {
        // TODO 这里固定使用了一个openID
        String openId = "omsLn0dOlKTbFpGPkCDiqWy79oJY";
        String token = Crypt3Des.encryptMode("phone="+mobile+"&timeStamp="+System.currentTimeMillis());
        System.out.println("token:"+token);
        String result = restTemplate.getForObject(WeChatUrlConstant.PHP_GET_USER_INFO+token,String.class,token);
        System.out.println("result:"+result);
//        PhpResult o = (PhpResult) JSON.parse(result);

//        System.out.println("这是个什么类："+o.getClass().getName());
        // TODO PHP那边还没有数据，再说吧
        //{"code":0,"msg":"未查询到信息"}
        //code==1查询成功，0没有查到信息，-1,"参数错误"，-2,"手机号错误"，-3,"请求失效"
        return null;
    }
}
