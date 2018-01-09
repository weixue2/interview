package com.huatu.tiku.interview.controller;

import com.google.common.collect.Maps;
import com.huatu.common.ErrorResult;
import com.huatu.common.exception.BizException;
import com.huatu.common.utils.reflect.BeanUtil;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午5:36
 **/
@Slf4j
@RestController
@RequestMapping(value = "/api/user",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
    @Autowired
    private  UserService userService;
    @PutMapping
    public void updateUserInfo(@RequestBody User user){
        log.info("id:{}",user.getId());
        if(user==null || user.getId()==0){
            throw new BizException(ErrorResult.create(403,"参数有误"));
        }
        userService.updateUser(user);
    }
//    @PostMapping
//    public void createUser(@RequestBody String openId){
//        log.info("id:{}",openId);
//        if(StringUtils.isBlank(openId)){
//            throw new BizException(ErrorResult.create(403,"参数有误"));
//        }
//        userService.createUser(openId);
//    }
    @GetMapping
    public Object getUserInfo(String openId){
        User user  =userService.getUser(openId);
        Map<String,User> map = Maps.newHashMapWithExpectedSize(4);
        map.put("user",user);
        return  map;
    }


}
