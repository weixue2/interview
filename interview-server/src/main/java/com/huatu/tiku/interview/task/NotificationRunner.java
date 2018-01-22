package com.huatu.tiku.interview.task;

import com.alibaba.fastjson.JSON;
import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.TemplateEnum;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.dto.ReadingTemp;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.template.MyTreeMap;
import com.huatu.tiku.interview.entity.template.TemplateMap;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.service.NotificationService;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @Scheduled(fixedDelay = 3 * 10 * 1000)
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
        String  json = redis.opsForValue().get(key);
        System.out.println("???");
        if(json.length()>2){
            List<ReadingTemp> rts = JSON.parseArray(json, ReadingTemp.class);
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
//                    templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.MorningReading);
//                    templateMsg.setUrl(BasicParameters.MorningReadingURL+notification.getId());
//                    templateMsg.setData(
//                            MyTreeMap.createMap(
//                                    new TemplateMap("first", WechatTemplateMsg.item("今日热点已新鲜出炉~", "#000000")),
//                                    new TemplateMap("keyword1", WechatTemplateMsg.item(u.getName(), "#000000")),
//                                    new TemplateMap("keyword2", WechatTemplateMsg.item(notification.getTitle(), "#000000")),
//                                    new TemplateMap("remark", WechatTemplateMsg.item("华图在线祝您顺利上岸！", "#000000"))
//                            )
//                    );
//                    System.out.println("随同了");
                    templateMsg = new WechatTemplateMsg(u.getOpenId(),TemplateEnum.HuaTu01);


//                    String templateMsgJson = JsonUtil.toJson(templateMsg);
//                    TemplateMsgResult msgResult = templateMsgService.sendTemplate(
//                            accessToken,
//                            templateMsgJson);
                    break;
                }
                case 3: {
                    templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.ReportHint);
                    templateMsg.setUrl(BasicParameters.ReportHintURL+notification.getId());
                    templateMsg.setData(
                            MyTreeMap.createMap(
                                    new TemplateMap("first", WechatTemplateMsg.item("亲爱的"+u.getName()+"同学，您购买的《2018国考封闭特训班》课程即将开课，请务必及时报到。", "#000000")),
//                                    new TemplateMap("keyword1", WechatTemplateMsg.item("2018国考封闭特训班", "#000000")),
                                    new TemplateMap("keyword2", WechatTemplateMsg.item("2018年2月2日", "#000000")),
                                    new TemplateMap("keyword3", WechatTemplateMsg.item("北京", "#000000")),
                                    new TemplateMap("keyword4", WechatTemplateMsg.item("400-817-6111", "#000000")),
                                    new TemplateMap("remark", WechatTemplateMsg.item("如有疑问，请及时与我们取得联系", "#000000"))
                            )
                    );
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
