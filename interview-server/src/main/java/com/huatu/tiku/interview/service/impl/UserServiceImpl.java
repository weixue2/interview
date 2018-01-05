package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.repository.UserRepository;
import com.huatu.tiku.interview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午4:32
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUser(String openId) {
        return userRepository.getUserByOpenIdAndStatus(openId,1);
    }
}
