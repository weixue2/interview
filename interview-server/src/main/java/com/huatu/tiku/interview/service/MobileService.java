package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/10 21:55
 * @Modefied By:
 */
public interface MobileService {
    User checkPHP(String mobile, String openId,HttpServletRequest request);
}
