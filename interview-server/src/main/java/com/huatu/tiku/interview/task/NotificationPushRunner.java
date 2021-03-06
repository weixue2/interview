package com.huatu.tiku.interview.task;

import com.alibaba.fastjson.JSON;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/23 21:15
 * @Description
 */
@Component
@Slf4j
public class NotificationPushRunner {

    private final String key = "readings";

    @Autowired
    StringRedisTemplate redis;

    @Autowired
    NotificationService notifyService;

    @Autowired
    UserService userService;

    @Autowired
    WechatTemplateMsgService templateMsgService;

    @Value("${notify_view}")
    private String notifyView;


    @Scheduled(fixedDelay = 1 * 1000)
    public void CheckNotification() {
        String json = redis.opsForValue().get(key);
        String accessToken = redis.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
        if (json.length() > 2) {
            List<ReadingTemp> rts = JSON.parseArray(json, ReadingTemp.class);
            if (rts != null) {
                List<ReadingTemp> pushList = new ArrayList<>();
                for (ReadingTemp rt : rts) {
                    if (rt.getStatus() && rt.getDate().before(new Date())) {
                        System.out.println("xxcv");
                        System.out.println(rt.getDate());
                        rt.setStatus(false);
                        pushList.add(rt);
                    }

                }
                // todo 电厂
                List<NotificationType> typeList = new ArrayList<>();
                if (!pushList.isEmpty()) {
                    for (ReadingTemp rt : pushList) {
                        typeList.add(notifyService.findOne(rt.getId()));
                    }
                    for (User u : userService.findAllUser()) {
                        Long cluss = userService.getCluss(u.getOpenId());
                        for (NotificationType nt : typeList) {
                            Boolean is = true;
                            if (!nt.getClassId().equals("0")) {

                                if (!nt.getClassId().equals(cluss)) {
                                    is = false;
                                }

                                if (!is) {
                                    continue;
                                }
                            } else if (cluss == 0) {
                                continue;
                            }
//                            NotificationType notification = notifyService.get(rt.getId());
                            WechatTemplateMsg templateMsg = null;
                            switch (nt.getType()) {
                                case 1: {
                                    System.out.println("发送消息。");
                                    break;
                                }
                                case 2: {
                                    System.out.println("随同了");
                                    templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.MorningReading);
                                    templateMsg.setUrl(notifyView + nt.getId());
                                    System.out.println(notifyView + nt.getId());
                                    templateMsg.setData(
                                            MyTreeMap.createMap(
                                                    new TemplateMap("first", WechatTemplateMsg.item("今日热点已新鲜出炉~", "#000000")),
                                                    new TemplateMap("keyword1", WechatTemplateMsg.item(u.getName(), "#000000")),
                                                    new TemplateMap("keyword2", WechatTemplateMsg.item(nt.getTitle(), "#000000")),
                                                    new TemplateMap("remark", WechatTemplateMsg.item("华图在线祝您顺利上岸！", "#000000"))
                                            )
                                    );

                                    break;
                                }
                                case 3: {
                                    System.out.println("随同了");
                                    log.info("随同了");
                                    templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.ReportHint);
                                    templateMsg.setUrl(notifyView + nt.getId());
                                    System.out.println(notifyView + nt.getId());
                                    templateMsg.setData(
                                            MyTreeMap.createMap(
                                                    new TemplateMap("first", WechatTemplateMsg.item("亲爱的" + u.getName() + "同学，您购买的《2018国考封闭特训班》课程即将开课，请务必及时报到。", "#000000")),
                                                    new TemplateMap("keyword1", WechatTemplateMsg.item("2018国考封闭特训班", "#000000")),
                                                    new TemplateMap("keyword2", WechatTemplateMsg.item("2018年2月2日", "#000000")),
                                                    new TemplateMap("keyword3", WechatTemplateMsg.item("北京", "#000000")),
                                                    new TemplateMap("keyword4", WechatTemplateMsg.item("400-817-6111", "#000000")),
                                                    new TemplateMap("remark", WechatTemplateMsg.item("如有疑问，请及时与我们取得联系", "#000000"))
                                            )
                                    );

                                    break;
                                }
                            }
//                        templateMsgService.sendTemplate(accessToken, JsonUtil.toJson(templateMsg));
                            RunPush runPush = new RunPush(accessToken, templateMsg);
                            runPush.run();
                        }
                    }
                }

            }
            insertRedis(rts);
        }
    }


    private void insertRedis(Object o) {
        redis.opsForValue().set(key, JSON.toJSONString(o));
        redis.expire(key, 2 * 3600 * 1000, TimeUnit.SECONDS);
    }


    class RunPush implements Runnable {
        private String accessToken;
        private WechatTemplateMsg templateMsg;

        public RunPush(String accessToken, WechatTemplateMsg templateMsg) {

            this.accessToken = accessToken;
            this.templateMsg = templateMsg;
        }

        @Override
        public void run() {
            System.out.println("推送了一个");
            TemplateMsgResult result = templateMsgService.sendTemplate(accessToken, JsonUtil.toJson(templateMsg));
            System.out.println("推送返回：" + result);
            try {
                this.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
//    private void PushNotification(ReadingTemp rt, NotificationType notification) {
//        System.out.println("普世了一轮");
//        String accessToken = redis.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
//        for (User u : userService.findAllUser()) {
//            System.out.println("用户名："+u.getName()+u.getOpenId());
//            WechatTemplateMsg templateMsg = null;
//            switch (rt.getType()) {
//                case 1: {
//                    System.out.println("发送消息。");
//                    break;
//                }
//                case 2: {
//                    System.out.println("随同了");
//                    templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.MorningReading);
//                    templateMsg.setUrl(notifyView+notification.getId());
//                    System.out.println(notifyView+notification.getId());
//                    templateMsg.setData(
//                            MyTreeMap.createMap(
//                                    new TemplateMap("first", WechatTemplateMsg.item("今日热点已新鲜出炉~", "#000000")),
//                                    new TemplateMap("keyword1", WechatTemplateMsg.item(u.getName(), "#000000")),
//                                    new TemplateMap("keyword2", WechatTemplateMsg.item(notification.getTitle(), "#000000")),
//                                    new TemplateMap("remark", WechatTemplateMsg.item("华图在线祝您顺利上岸！", "#000000"))
//                            )
//                    );
//
//                    break;
//                }
//                case 3: {
//                    System.out.println("随同了");
//                    log.info("随同了");
//                    templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.ReportHint);
//                    templateMsg.setUrl(notifyView+notification.getId());
//                    System.out.println(notifyView+notification.getId());
//                    templateMsg.setData(
//                            MyTreeMap.createMap(
//                                    new TemplateMap("first", WechatTemplateMsg.item("亲爱的"+u.getName()+"同学，您购买的《2018国考封闭特训班》课程即将开课，请务必及时报到。", "#000000")),
////                                    new TemplateMap("keyword1", WechatTemplateMsg.item("2018国考封闭特训班", "#000000")),
//                                    new TemplateMap("keyword2", WechatTemplateMsg.item("2018年2月2日", "#000000")),
//                                    new TemplateMap("keyword3", WechatTemplateMsg.item("北京", "#000000")),
//                                    new TemplateMap("keyword4", WechatTemplateMsg.item("400-817-6111", "#000000")),
//                                    new TemplateMap("remark", WechatTemplateMsg.item("如有疑问，请及时与我们取得联系", "#000000"))
//                            )
//                    );
//
//                    break;
//                }
//            }
//            templateMsgService.sendTemplate(accessToken, JsonUtil.toJson(templateMsg));
//
//        }
//    }