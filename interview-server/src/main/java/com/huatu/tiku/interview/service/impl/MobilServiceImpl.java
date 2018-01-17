package com.huatu.tiku.interview.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonObject;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.result.PhpResult;
import com.huatu.tiku.interview.service.MobileService;
import com.huatu.tiku.interview.util.Crypt3Des;
import net.sf.json.JSONObject;
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
    public User checkPHP(String mobile,String openId) {
        // TODO 这里固定使用了一个openID
        openId = "omsLn0dOlKTbFpGPkCDiqWy79oJY";
        String token = Crypt3Des.encryptMode("phone="+mobile+"&timeStamp="+System.currentTimeMillis());
        System.out.println("token:"+token);
        // 请求php
        String result = restTemplate.getForObject(WeChatUrlConstant.PHP_GET_USER_INFO+token,String.class,token);
        System.out.println("result:"+result);
        JSONObject jsonobject = JSONObject.fromObject(result);
        PhpResult phpResult= (PhpResult)JSONObject.toBean(jsonobject,PhpResult.class);
        // TODO 验证啊。。。
        String userInfo = Crypt3Des.decryptMode(phpResult.getData());
        JSONObject jsonObject2 = JSONObject.fromObject(userInfo);
        String sex = jsonObject2.get("sex").toString();
        String phone = jsonObject2.get("phone").toString();
        User user = new User();
        user.setSex(Integer.valueOf(sex));
        user.setPhone(phone);
        user.setOpenId(openId);
        System.out.println(user);

        return user;
    }
}
