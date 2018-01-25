package com.huatu.tiku.interview.controller.api;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.constant.TemplateEnum;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.entity.template.MyTreeMap;
import com.huatu.tiku.interview.entity.template.TemplateMap;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.service.MobileService;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.LogPrint;
import com.huatu.tiku.interview.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午5:36
 **/

//@CrossOrigin
@Slf4j
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MobileService mobileService;

    @Autowired
    StringRedisTemplate redis;

    @Autowired
    WechatTemplateMsgService templateMsgService;
    @Value("${notify_view}")
    private String notifyView;


    @LogPrint
    @GetMapping("getMobile")
    public Result getMobile(String mobile, String openId, HttpServletRequest req) {
        // xml请求解析
//        Map<String, String> requestMap = MessageUtil.parseXml(req);
//        String openId = requestMap.get("FromUserName");
//        System.out.println("OpenId等于："+req.getSession().getAttribute("openId"));
//        mobileService.checkPHP(mobile,openId);
        log.info("openId:欧喷爱滴"+openId);
        return Result.ok(mobileService.checkPHP(mobile, openId, req));
    }

    @LogPrint
    @GetMapping(value = "getNext")
    public Result userCaptcha(String mobile, String captcha) {
        mobileService.userCaptcha(mobile, captcha);
//        return SuccessMessage.create("验证通过");
        return Result.build(ResultEnum.CAPTCHA_PASS);
    }
//    @PutMapping
//    public void updateUserInfo(@RequestBody User user,HttpServletRequest request){
//        log.info("id:{}",user.getId());
//        if(user==null || user.getId()==0){
//            throw new BizException(ErrorResult.create(403,"参数有误"));
//        }
//        userService.updateUser(user,request);
//    }


    //    @PostMapping
//    public void createUser(@RequestBody String openId){
//        log.info("id:{}",openId);
//        if(StringUtils.isBlank(openId)){
//            throw new ReqException(ErrorResult.create(403,"参数有误"));
//        }
//        userService.createUser(openId);
//    }

    @LogPrint
    @PostMapping
    public Result updateUser(@RequestBody User user, HttpServletRequest request) {
        if (user.getAgreement() == null){
            user.setAgreement(false);
        }
        if(!user.getAgreement()){
            return Result.build(ResultEnum.Agreement_ERROR);
        }
        log.info(user.toString());
        user.setStatus(1);
        log.info(user.toString());
        return userService.updateUser(user, request) ? Result.ok() : Result.build(ResultEnum.INSERT_FAIL);
    }

    @LogPrint
    @GetMapping
    public Result getUserInfo(String openId) {
//        System.out.println("你知道吗 openId:"+openId);
        User user = userService.getUser(openId);
//        Map<String, User> map = Maps.newHashMapWithExpectedSize(4);
//        map.put("user", user);
        if (user == null){
            user = new User();
            user.setKeyContact("");
            user.setNation("");
            user.setIdCard("");
            user.setName("");
            user.setSex(-1);
            user.setPhone("");
            user.setStatus(-1);
            user.setPregnancy(null);
            user.setPhpUserId(0);

        }
        return Result.ok(user);
    }

    @LogPrint
    @GetMapping("pushNotify")
    public Result pushNotify(){
        String accessToken = redis.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
        WechatTemplateMsg templateMsg ;
        for (User u : userService.findAllUser()) {
            templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.MorningReading);
            templateMsg.setUrl(notifyView+6);
            templateMsg.setData(
                    MyTreeMap.createMap(
                            new TemplateMap("first", WechatTemplateMsg.item("今日热点已新鲜出炉~", "#000000")),
                            new TemplateMap("keyword1", WechatTemplateMsg.item(u.getName(), "#000000")),
                            new TemplateMap("keyword2", WechatTemplateMsg.item("123", "#000000")),
                            new TemplateMap("remark", WechatTemplateMsg.item("华图在线祝您顺利上岸！", "#000000"))
                    )
            );
            templateMsgService.sendTemplate(accessToken, JsonUtil.toJson(templateMsg));

        }
        for (User u : userService.findAllUser()) {
            templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.MorningReading);
            templateMsg.setUrl(notifyView+6);
            templateMsg.setData(
                    MyTreeMap.createMap(
                            new TemplateMap("first", WechatTemplateMsg.item("今日热点已新鲜出炉~", "#000000")),
                            new TemplateMap("keyword1", WechatTemplateMsg.item(u.getName(), "#000000")),
                            new TemplateMap("keyword2", WechatTemplateMsg.item("1233", "#000000")),
                            new TemplateMap("remark", WechatTemplateMsg.item("华图在线祝您顺利上岸！", "#000000"))
                    )
            );
            templateMsgService.sendTemplate(accessToken, JsonUtil.toJson(templateMsg));

        }
        return Result.ok();
    }



}
