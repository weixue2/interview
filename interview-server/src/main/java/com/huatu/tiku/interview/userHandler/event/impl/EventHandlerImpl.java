package com.huatu.tiku.interview.userHandler.event.impl;

import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.po.SignIn;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
import com.huatu.tiku.interview.repository.SignInRepository;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.userHandler.event.EventHandler;
import com.huatu.tiku.interview.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.aspectj.weaver.patterns.NotTypePattern;
import org.springframework.beans.factory.annotation.Autowired;
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
    private NotificationTypeRepository notificationTypeRepository;

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
        a.setUrl(BasicParameters.LINK_SUBSCRIBE_001 + fromUserName);
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
        String h = new SimpleDateFormat("HH").format(new Date());
        String str;
        //设置签到时间
        //if (Integer.parseInt(h) < 9 && Integer.parseInt(h) > 8) {
        if (System.currentTimeMillis() % 2 == 1) {
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
                    .content("签到失败")
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
            List<NotificationType> notTypePatterns = notificationTypeRepository.findByBizStatusAndStatus
                    (new Sort(Sort.Direction.DESC, "gmtModify"), WXStatusEnum.BizStatus.ONLINE.getBizSatus(), WXStatusEnum.Status.NORMAL.getStatus());
            str = WxMpXmlOutMessage
                    .IMAGE()
                    .mediaId(notTypePatterns.get(0).getWxImageId())
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build()
                    .toXml();
        } else if ("user_info".equals(requestMap.get("EventKey"))) {
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("正在开发")
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
        return str;
    }
}
