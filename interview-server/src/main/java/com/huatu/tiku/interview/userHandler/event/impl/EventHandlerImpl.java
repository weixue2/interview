package com.huatu.tiku.interview.userHandler.event.impl;

import com.huatu.common.utils.date.DateFormatUtil;
import com.huatu.common.utils.date.DateUtil;
import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.message.TextMessage;
import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;
import com.huatu.tiku.interview.entity.po.SignIn;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.repository.OnlineCourseArrangementRepository;
import com.huatu.tiku.interview.repository.SignIdRepository;
import com.huatu.tiku.interview.userHandler.event.EventHandler;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.util.MessageUtil;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
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
    private SignIdRepository signIdRepository;
    @Autowired
    private OnlineCourseArrangementRepository onlineCourseArrangementRepository;

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
        a.setUrl(BasicParameters.LINK_SUBSCRIBE_001);
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
    public String signIn(Map<String, String> requestMap) {
        String h = new SimpleDateFormat("HH").format(new Date());
        String str;
        //if (Integer.parseInt(h) < 9 && Integer.parseInt(h) > 8) {
        if (System.currentTimeMillis() % 2 == 1) {
            log.info("开始签到");
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("签到成功")
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build().toXml();
            SignIn signIn = new SignIn();
            signIn.setOpenId(requestMap.get("FromUserName"));
            signIn.setSignTime(new Date());
            signIn.setBizStatus(1);
            signIn.setStatus(1);
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
            List<OnlineCourseArrangement> onlineCourseArrangements = onlineCourseArrangementRepository.findByBizStatusAndStatus(new Sort(Sort.Direction.DESC, "UpdateTimestamp"), WXStatusEnum.BizStatus.NORMAL, WXStatusEnum.Status.ONLINE);
            str = WxMpXmlOutMessage
                    .IMAGE()
                    .mediaId(onlineCourseArrangements.get(0).getWxImageId())
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
