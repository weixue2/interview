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
import com.huatu.tiku.interview.service.NotificationService;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/19 23:59
 * @Description 之前写的定时器太丑陋了，没眼看，推翻重写吧
 */
@Component
@Slf4j
public class NotificationRunner {

    private final String key = "readings";

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
        System.out.println(list.size());
        if (!list.isEmpty()) {
            List<ReadingTemp> rts = new ArrayList<>();
            for (NotificationType mr : list) {
                rts.add(new ReadingTemp(mr.getId(), mr.getPushTime(), true, mr.getType()));
            }
            insertRedis(rts);
        } else {
            insertRedis("");
        }
    }

    @Scheduled(fixedDelay = 10 * 1000)
    public void CheckNotification() {
        Object o = redis.opsForValue().get(key);
        List<ReadingTemp> rts = JSON.parseArray(o.toString(), ReadingTemp.class);
        if (rts != null) {
            for (ReadingTemp rt : rts) {
                if (rt.getStatus() && rt.getDate().before(new Date())) {
                    System.out.println(rt.getDate());
                    rt.setStatus(false);
                    PushNotification(rt, notifyService.get(rt.getId()));
                }
            }
        }
        insertRedis(rts);
    }

    private void PushNotification(ReadingTemp rt, NotificationType notification) {
        String accessToken = redis.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
        for (User u : userService.findAllUser()) {
            WechatTemplateMsg templateMsg = null;
            switch (rt.getType()) {
                case 1: {
                    System.out.println("发送消息。");
                    break;
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
                    break;
                }
                case 3: {
                    break;
                }
            }
            templateMsgService.sendTemplate(accessToken, JsonUtil.toJson(templateMsg));
        }
    }

    private void insertRedis(Object o) {
        redis.opsForValue().set(key, JSON.toJSONString(o));
        redis.expire(key, 2 * 3600 * 1000, TimeUnit.SECONDS);
    }
}
