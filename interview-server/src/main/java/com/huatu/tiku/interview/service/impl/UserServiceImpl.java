package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.exception.ReqException;
import com.huatu.tiku.interview.repository.UserRepository;
import com.huatu.tiku.interview.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

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
    public Boolean updateUser(User user,HttpServletRequest request) {
//        Object o = request.getSession().getAttribute("openId");
//        if(o == null){
//            throw new ReqException(ResultEnum.OPENID_ERROR);
//        }
//        String openId = o.toString();
//        String openId = "od2aM0j6XSIwjAt2fExHeegjOWn8";

        User user_ = userRepository.findByOpenId(user.getOpenId());
        if(user_ != null){
            user_.setPhone(user.getPhone());
            user_.setSex(user.getSex());
            user_.setName(user.getName());
            user_.setIdCard(user.getIdCard());
            user_.setPregnancy(user.getPregnancy());
            user_.setNation(user.getNation());
            user.setKeyContact(user.getKeyContact());
        }
        System.out.println(user);
        return userRepository.save(user_)==null ? false:true;
    }

    @Override
    public void createUser(String openId) {
        User user = new User();
        user.setOpenId(openId);
        userRepository.save(user);
    }

    @Override
    public User getUser(String openId) {
        return userRepository.getUserByOpenIdAndStatus(openId,1);
    }

    @Override
    public User getUserByOpenId(String openId) {
        return userRepository.findByOpenId(openId);
    }

    @Override
    public Object findAllUser() {
        return userRepository.findAll();
    }
}
