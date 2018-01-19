package com.huatu.tiku.interview.task;

import com.alibaba.fastjson.JSON;
import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.TemplateEnum;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.dto.ReadingTemp;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.service.MessageService;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 10:52
 * @Description
 */
@Component
@Order
public class MorningReadingPushRunner implements CommandLineRunner {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MessageService messageService;

    @Autowired
    WechatTemplateMsgService templateMsgService;

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) {
        new Timer().schedule(new RemindTask(), 0, 30000);
    }

    class RemindTask extends TimerTask {
        @Override
        public void run() {
            String accessToken = redisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
//                accessToken = "6_xsXvmd5iX-22XaVXgCmlUzJ8V6YKssh7XfaVtZXe6GdzSydUPcKT8i4G2ULClF9Th9wmQQ9LUOGrZI8kxj330SApNk9HcEYSei1sD9F7daYj7Q3IryQQffHC9IMLIYgAGASPF";
//            WechatTemplateMsg templateMsg = new WechatTemplateMsg("omsLn0dOlKTbFpGPkCDiqWy79oJY",TemplateEnum.No_2);
//
//            String templateMsgJson = JsonUtil.toJson(templateMsg);
//            TemplateMsgResult msgResult = templateMsgService.sendTemplate(
//                    accessToken,
//                    templateMsgJson);
//            System.out.println(msgResult);
            try {
                // TODO 获取时间表
                System.out.println("dtdt");
                Object o = redisTemplate.opsForValue().get("readings");
                if (o != null) {

                    List<ReadingTemp> rts = JSON.parseArray(o.toString(), ReadingTemp.class);
                    Calendar cal_a = Calendar.getInstance();
                    Calendar cal_b = Calendar.getInstance();
                    cal_b.setTime(new Date());
                    for (ReadingTemp rt : rts) {
//                        System.out.println(rt);
//                        System.out.println(cal_a.get(Calendar.HOUR_OF_DAY));
//                        System.out.println(cal_b.get(Calendar.HOUR_OF_DAY));
//                        System.out.println(cal_a.get(Calendar.HOUR));
//                        System.out.println(cal_b.get(Calendar.HOUR));
                        if (rt.getDate() == null){
                            continue;
                        }
                        cal_a.setTime(rt.getDate());
                        if (cal_a.get(Calendar.YEAR) == cal_b.get(Calendar.YEAR)) {
                            if (cal_a.get(Calendar.MONTH) == cal_b.get(Calendar.MONTH)) {
                                if (cal_a.get(Calendar.DAY_OF_MONTH) == cal_b.get(Calendar.DAY_OF_MONTH)) {
                                    if (cal_a.get(Calendar.HOUR_OF_DAY) == cal_b.get(Calendar.HOUR_OF_DAY)) {
                                        if (cal_a.get(Calendar.MINUTE) == cal_b.get(Calendar.MINUTE)) {
//                                            if (cal_a.get(Calendar.SECOND) == cal_b.get(Calendar.SECOND)) {
                                                if(rt.getStatus()){
                                                    List<User> allUser = userService.findAllUser();
                                                    for(User u : allUser){
                                                        WechatTemplateMsg templateMsg = null;
                                                        switch (rt.getType()){
                                                            case 1:{

                                                            }
                                                            case 2:{
                                                                templateMsg = new WechatTemplateMsg(u.getOpenId(),TemplateEnum.No_2);
                                                            }
                                                            case 3:{

                                                            }
                                                        }

                                                        String templateMsgJson = JsonUtil.toJson(templateMsg);
                                                        templateMsgService.sendTemplate(
                                                                accessToken,
                                                                templateMsgJson);
                                                    }

                                                    rt.setStatus(false);
                                                }

//                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    String json = JSON.toJSONString(rts);
                    redisTemplate.opsForValue().set("readings", json);
                }
            } catch (Exception e) {
                System.out.println("获取时间表出错");
            }
        }

    }


}
