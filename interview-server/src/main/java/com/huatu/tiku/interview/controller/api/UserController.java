package com.huatu.tiku.interview.controller.api;

import com.google.common.collect.Maps;
import com.huatu.common.ErrorResult;
import com.huatu.common.SuccessMessage;
import com.huatu.common.exception.BizException;
import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.MobileService;
import com.huatu.tiku.interview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午5:36
 **/

@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value = "/api/user",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
    @Autowired
    private  UserService userService;

    @Autowired
    private MobileService mobileService;
    @PutMapping
    public void updateUserInfo(@RequestBody User user,HttpServletRequest request){
        log.info("id:{}",user.getId());
        if(user==null || user.getId()==0){
            throw new BizException(ErrorResult.create(403,"参数有误"));
        }
        userService.updateUser(user,request);
    }

    @PostMapping
    public Result updateUser(@RequestBody User user,HttpServletRequest request){

        return userService.updateUser(user,request)? Result.ok(): Result.build(ResultEnum.INSERT_FAIL);
    }


//    @PostMapping
//    public void createUser(@RequestBody String openId){
//        log.info("id:{}",openId);
//        if(StringUtils.isBlank(openId)){
//            throw new ReqException(ErrorResult.create(403,"参数有误"));
//        }
//        userService.createUser(openId);
//    }


    @GetMapping
    public Result getUserInfo(String openId){
//        System.out.println("你知道吗 openId:"+openId);
        User user  =userService.getUser(openId);
        Map<String,User> map = Maps.newHashMapWithExpectedSize(4);
        map.put("user",user);
        return  Result.ok(map);
    }

    @GetMapping("getMobile")
    public Result getMobile(String mobile,String openId,HttpServletRequest req){
        // xml请求解析
//        Map<String, String> requestMap = MessageUtil.parseXml(req);
//        String openId = requestMap.get("FromUserName");
//        System.out.println("OpenId等于："+req.getSession().getAttribute("openId"));
//        mobileService.checkPHP(mobile,openId);
        return Result.ok(mobileService.checkPHP(mobile,openId,req));
    }
    @GetMapping(value = "getNext")
    public Result userCaptcha(String mobile, String captcha) {
        mobileService.userCaptcha(mobile, captcha);
//        return SuccessMessage.create("验证通过");
        return Result.build(ResultEnum.CAPTCHA_PASS);
    }

}
