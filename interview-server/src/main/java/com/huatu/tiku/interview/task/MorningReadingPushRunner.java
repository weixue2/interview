package com.huatu.tiku.interview.task;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 10:52
 * @Description
 */
@Component
@Order
public class MorningReadingPushRunner{
//public class MorningReadingPushRunner implements CommandLineRunner {

//    @Autowired
//    StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    MessageService messageService;
//
//    @Autowired
//    WechatTemplateMsgService templateMsgService;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    NotificationService notificationService;
//
//    @Override
//    public void run(String... args) {
//        new Timer().schedule(new RemindTask(), 0, 30000);
//    }
//
//    class RemindTask extends TimerTask {
//        @Override
//        public void run() {
//            String accessToken = stringRedisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
//            try {
//                // TODO 获取时间表
//                Object o = stringRedisTemplate.opsForValue().get("readings");
//                if (o != null) {
//
//                    List<ReadingTemp> rts = JSON.parseArray(o.toString(), ReadingTemp.class);
//                    Calendar cal_a = Calendar.getInstance();
//                    Calendar cal_b = Calendar.getInstance();
//                    cal_b.setTime(new Date());
//                    for (ReadingTemp rt : rts) {
//                        if (rt.getDate() == null){
//                            continue;
//                        }
//                        cal_a.setTime(rt.getDate());
//                        if (cal_a.get(Calendar.YEAR) == cal_b.get(Calendar.YEAR)) {
//                            if (cal_a.get(Calendar.MONTH) == cal_b.get(Calendar.MONTH)) {
//                                if (cal_a.get(Calendar.DAY_OF_MONTH) == cal_b.get(Calendar.DAY_OF_MONTH)) {
//                                    if (cal_a.get(Calendar.HOUR_OF_DAY) == cal_b.get(Calendar.HOUR_OF_DAY)) {
//                                        if (cal_a.get(Calendar.MINUTE) == cal_b.get(Calendar.MINUTE)) {
////                                            if (cal_a.get(Calendar.SECOND) == cal_b.get(Calendar.SECOND)) {
//                                                if(rt.getStatus()){
//                                                    List<User> allUser = userService.findAllUser();
//                                                    for(User u : allUser){
//                                                        WechatTemplateMsg templateMsg = null;
//                                                        switch (rt.getType()){
//                                                            case 1:{
//
//                                                            }
//                                                            case 2:{
//                                                                NotificationType notification = notificationService.get(rt.getId());
//                                                                templateMsg = new WechatTemplateMsg(u.getOpenId(),TemplateEnum.MorningReading);
//                                                                templateMsg.setData(
//                                                                        MyTreeMap.createMap(
//                                                                                new TemplateMap("first", WechatTemplateMsg.item(notification.getTitle(),"#000000")),
//                                                                                new TemplateMap("keyword1", WechatTemplateMsg.item(u.getName(),"#000000")),
//                                                                                new TemplateMap("remark", WechatTemplateMsg.item("华图教育发给你的","#000000"))
//                                                                        )
//                                                                );
//                                                                System.out.println(u.getName());
//                                                            }
//                                                            case 3:{
//
//                                                            }
//                                                        }
//
//                                                        String templateMsgJson = JsonUtil.toJson(templateMsg);
//                                                        templateMsgService.sendTemplate(
//                                                                accessToken,
//                                                                templateMsgJson);
//                                                    }
//                                                    rt.setStatus(false);
//                                                }
////                                            }
//                                        } } } } }
//                    }
//                    String json = JSON.toJSONString(rts);
//                    stringRedisTemplate.opsForValue().set("readings", json);
//                    stringRedisTemplate.expire("readings",2 * 3600 * 1000, TimeUnit.SECONDS);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                System.out.println("获取时间表出错");
//            }
//        }
//
//    }


}
