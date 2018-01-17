package com.huatu.tiku.interview.userHandler.event.impl;

import com.huatu.common.utils.date.DateFormatUtil;
import com.huatu.common.utils.date.DateUtil;
import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.message.TextMessage;
import com.huatu.tiku.interview.entity.po.User;
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
    UserService userService;

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

//    @Override
//    public String subscribeEvent(Map<String, String> requestMap) {
//        String fromUserName = requestMap.get("FromUserName");
//        NewsMessage newsMessage = new NewsMessage(requestMap);
//        //创建用户，记录openId TODO 校验是否存在，存在说明是重复关注不处理
//        User user = userService.getUserByOpenId(fromUserName);
////        System.out.println(user.getOpenId());
//        if(user == null){
//            userService.createUser(fromUserName);
//        }
//
//
//        List<Article> articleList = new ArrayList<Article>();
//        //测试单图文回复
//        Article article = new Article();
//        article.setTitle("谢谢您的关注！");
//        // 图文消息中可以使用QQ表情、符号表情
//        article.setDescription("点击图文可以跳转到华图首页");
//        // 将图片置为空
//        article.setPicUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1515474563395&di=7fd2315a708740c4b8ed01b68ff8d1d4&imgtype=0&src=http%3A%2F%2Fwww.zhlzw.com%2FUploadFiles%2FArticle_UploadFiles%2F201204%2F20120412123904521.jpg");
//        article.setUrl("http://v.huatu.com");
//        articleList.add(article);
//        newsMessage.setArticleCount(articleList.size());
//        newsMessage.setArticles(articleList);
//        return MessageUtil.MessageToXml(newsMessage);
//    }

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
        if (Integer.parseInt(h) < 9 && Integer.parseInt(h) > 8) {
            log.info("开始签到");
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("签到成功")
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build().toXml();
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

    @Override
    public String eventClick(Map<String, String> requestMap) {
        String str=null;
        if ("course".equals(requestMap.get("EventKey"))) {
            str = WxMpXmlOutMessage
                    .IMAGE()
                    .mediaId("Bfmnlf2k6LOgXFHII34PCz1BCErjN4IuK1Q3-bkrNav577jG8EWW9e_CAnsOAERR")
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build().toXml();
        }
        return str;
    }
}
