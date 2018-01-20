package com.huatu.tiku.interview.task;

import com.alibaba.fastjson.JSON;
import com.huatu.tiku.interview.constant.TemplateEnum;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.dto.ReadingTemp;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.template.MyTreeMap;
import com.huatu.tiku.interview.entity.template.TemplateMap;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
import com.huatu.tiku.interview.service.NotificationService;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/19 23:59
 * @Description 之前写的定时器太丑陋了，没眼看，推翻重写吧
 */
@Component
@Slf4j
public class NotificationRunner {

    @Autowired
    StringRedisTemplate redis;

    @Autowired
    NotificationService notifyService;

    @Autowired
    UserService userService;

    @Autowired
    WechatTemplateMsgService templateMsgService;


    @Scheduled(fixedDelay = 2 * 3600 * 1000)
    public void GetNotification() {
        List<NotificationType> list = notifyService.findByPushTime();
        if (!list.isEmpty()) {
            List<ReadingTemp> rts = new ArrayList<>();
            for (NotificationType mr : list) {
                rts.add(new ReadingTemp(mr.getId(), mr.getPushTime(), true, mr.getType()));
            }
            redis.opsForValue().set("readings", JSON.toJSONString(rts));
//                redis.expire("readings",2 * 3600 * 1000, TimeUnit.SECONDS);
        }
    }


    @Scheduled(fixedDelay = 10 * 1000)
    public void CheckNotification() {
        Object o = redis.opsForValue().get("readings");
        List<ReadingTemp> rts = JSON.parseArray(o.toString(), ReadingTemp.class);
        for (ReadingTemp rt : rts) {
            if (rt.getStatus() && rt.getDate().before(new Date())) {
                rt.setStatus(false);
                PushNotification(rt,notifyService.get(rt.getId()));
            }
        }
        redis.opsForValue().set("readings", JSON.toJSONString(rts));
    }


    private void PushNotification(ReadingTemp rt, NotificationType notification) {
        String accessToken = redis.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
        for (User u : userService.findAllUser()) {
            WechatTemplateMsg templateMsg = null;
            switch (rt.getType()) {
                case 1: {
                    System.out.println("发送消息。");
                }
                case 2: {

                    templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.MorningReading);
                    templateMsg.setData(
                            MyTreeMap.createMap(
                                    new TemplateMap("first", WechatTemplateMsg.item(notification.getTitle(), "#000000")),
                                    new TemplateMap("keyword1", WechatTemplateMsg.item(u.getName(), "#000000")),
                                    new TemplateMap("remark", WechatTemplateMsg.item("华图教育发给你的", "#000000"))
                            )
                    );
                }
                case 3: {

                }
            }
            templateMsgService.sendTemplate(accessToken, JsonUtil.toJson(templateMsg));
        }
    }
}
