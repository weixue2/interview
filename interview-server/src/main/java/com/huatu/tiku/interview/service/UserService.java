package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.User;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午4:31
 **/
public interface UserService {
     void updateUser(User user);
    User getUser(String openId);
}
