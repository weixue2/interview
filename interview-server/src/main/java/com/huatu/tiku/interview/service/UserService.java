package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午4:31
 **/
public interface UserService {
    Boolean updateUser(User user, HttpServletRequest request);

    void createUser(String openId);

    User getUser(String openId);

    User getUserByOpenId(String openId);

    List<User> findAllUser(String content);
    List<User> findAllUser();

    Long getCluss(String openId);
}
