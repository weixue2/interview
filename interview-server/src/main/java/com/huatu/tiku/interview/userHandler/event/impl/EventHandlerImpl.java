package com.huatu.tiku.interview.userHandler.event.impl;

import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.po.SignIn;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.repository.LearningReportRepository;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
import com.huatu.tiku.interview.repository.SignInRepository;
import com.huatu.tiku.interview.repository.UserRepository;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.userHandler.event.EventHandler;
import com.huatu.tiku.interview.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/12 10:07
 * @Modefied By:
 * 用户事件处理类
 */
@Component
@Slf4j
public class EventHandlerImpl implements EventHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private SignInRepository signInRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationTypeRepository notificationTypeRepository;
    @Autowired
    private LearningReportRepository learningReportRepository;
    @Value("${phone_check}")
    private String phoneCheck;


    @Override
    public String subscribeHandler(Map<String, String> requestMap) {
        // TODO 因为可以验证取关的事件，所以这里的逻辑可以删掉
        String fromUserName = requestMap.get("FromUserName");
        User user = userService.getUserByOpenId(fromUserName);
        if (user == null) {
            userService.createUser(fromUserName);
        }
        NewsMessage nm = new NewsMessage(requestMap);
        List<Article> as = new ArrayList<>();
        Article a = new Article();
        a.setTitle("谢谢您的关注！!");
        a.setDescription("点击图文可以跳转到华图首页");
        a.setPicUrl(BasicParameters.IMAGE_SUBSCRIBE_001);
        //这里跳转前端验证
        a.setUrl(phoneCheck + fromUserName);
        as.add(a);
        nm.setArticleCount(as.size());
        nm.setArticles(as);
        return MessageUtil.MessageToXml(nm);
    }


    @Override
    public String unsubscribeHandler(Map<String, String> requestMap) {
        return null;
    }

    /**
     * 这东西太他妈太反人类了
     *
     * @param requestMap
     * @return
     */
    @Override
    public String signInHandler(Map<String, String> requestMap) {
        String str;
        log.info("开始判断该二维码是否我为华图官方签到二维码:" + requestMap.get("EventKey"));
        if ("sign_in".equals(requestMap.get("EventKey"))) {
            String h = new SimpleDateFormat("HH").format(new Date());
            //设置签到时间    08:00-09:00    13:00-14:00   18:00-19:00
            if (Integer.parseInt(h) < 9 && Integer.parseInt(h) >= 8 || Integer.parseInt(h) < 14 && Integer.parseInt(h) >= 13 || Integer.parseInt(h) < 19 && Integer.parseInt(h) >= 18) {
                //if (System.currentTimeMillis() % 2 == 1) {
                log.info("开始签到");
                str = WxMpXmlOutMessage
                        .TEXT()
                        .content("签到成功")
                        .fromUser(requestMap.get("ToUserName"))
                        .toUser(requestMap.get("FromUserName"))
                        .build()
                        .toXml();
                SignIn signIn = new SignIn();
                signIn.setOpenId(requestMap.get("FromUserName"));
                signIn.setSignTime(new Date());
                signIn.setBizStatus(1);
                signIn.setStatus(1);
                signInRepository.save(signIn);
            } else {
                log.info("签到失败");
                str = WxMpXmlOutMessage
                        .TEXT()
                        .content("签到时间已过")
                        .fromUser(requestMap.get("ToUserName"))
                        .toUser(requestMap.get("FromUserName"))
                        .build().toXml();
            }
        } else {
            log.info("非签到二维码");
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("该二维码不可用于签到")
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build().toXml();
        }
        return str;
    }

    /**
     * 处理点击事件
     *
     * @param requestMap
     * @return
     */
    @Override
    public String eventClick(Map<String, String> requestMap) {
        String str = null;
        if ("course".equals(requestMap.get("EventKey"))) {
            User user = userRepository.findByOpenId(requestMap.get("FromUserName"));
            if ((user == null || user.getStatus() != 1)) {
                log.info("----查询不到用户信息----");
                str = WxMpXmlOutMessage
                        .TEXT()
                        .content("抱歉，经系统核实您的手机号未购买“2018国考封闭特训班”~若有疑问，请联系客服：400-817-6111")
                        .fromUser(requestMap.get("ToUserName"))
                        .toUser(requestMap.get("FromUserName"))
                        .build()
                        .toXml();
            } else {
                List<NotificationType> notTypePatterns = notificationTypeRepository.findByBizStatusAndStatus
                        (new Sort(Sort.Direction.DESC, "gmtModify"), WXStatusEnum.BizStatus.ONLINE.getBizSatus(), WXStatusEnum.Status.NORMAL.getStatus());
                for (NotificationType notificationType : notTypePatterns) {
                    if (StringUtils.isNotEmpty(notificationType.getWxImageId())) {
                        log.info("----展示图片----");
                        log.info("----图片id:" + notificationType.getWxImageId());
                        str = WxMpXmlOutMessage
                                .IMAGE()
                                .mediaId(notTypePatterns.get(0).getWxImageId())
                                .fromUser(requestMap.get("ToUserName"))
                                .toUser(requestMap.get("FromUserName"))
                                .build()
                                .toXml();
                        break;
                    }
                }
            }
        } else if ("conn_service".equals(requestMap.get("EventKey"))) {
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("客服电话：400-817-6111")
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build()
                    .toXml();
        } else {
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("正在开发")
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build()
                    .toXml();
        }
        log.info("----返回的xml:" + str);
        return str;
    }
}
