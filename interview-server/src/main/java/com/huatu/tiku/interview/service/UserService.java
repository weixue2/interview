package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.User;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午4:31
 **/
public interface UserService {
    Boolean updateUser(User user);
     void createUser(String openId);
    User getUser(String openId);
     User getUserByOpenId(String openId);
}
