package com.huatu.tiku.interview.userHandler.message.impl;

import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.TemplateEnum;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.message.TextMessage;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.userHandler.message.MessageHandler;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.MessageUtil;
import com.huatu.tiku.interview.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/12 9:56
 * @Modefied By:
 * 消息处理类
 */
@Component
@Slf4j
public class MessageHandlerImpl implements MessageHandler {

    @Autowired
    private WechatTemplateMsgService templateMsgService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    private ServletContext servletContext;

    @Override
    public String TextMessageHandler(Map<String, String> requestMap){
        String accessToken = redisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
//        accessToken = "6_xsXvmd5iX-22XaVXgCmlUzJ8V6YKssh7XfaVtZXe6GdzSydUPcKT8i4G2ULClF9Th9wmQQ9LUOGrZI8kxj330SApNk9HcEYSei1sD9F7daYj7Q3IryQQffHC9IMLIYgAGASPF";
        System.out.println("accessToken:"+accessToken);
        if(requestMap.get("Content").equals("?")){
            //            String templateMsgJson = WechatTemplateMsg.getJson(TemplateEnum.No_2,requestMap);
            WechatTemplateMsg templateMsg = new WechatTemplateMsg(TemplateEnum.DailyReport,requestMap);

            String templateMsgJson = JsonUtil.toJson(templateMsg);
            TemplateMsgResult msgResult = templateMsgService.sendTemplate(
                    accessToken,
                    templateMsgJson);
            return null;
        }
        if(!requestMap.get("Content").equals("get")){
//            //这个是直接生成String
////            String templateMsgJson = WechatTemplateMsg.getJson(TemplateEnum.No_2,requestMap);
//            WechatTemplateMsg templateMsg = new WechatTemplateMsg(TemplateEnum.No_2,requestMap);
//
//            String templateMsgJson = JsonUtil.toJson(templateMsg);
//            TemplateMsgResult msgResult = templateMsgService.sendTemplate(
//                    accessToken,
//                    templateMsgJson);
//            return null;
            NewsMessage nm = new NewsMessage(requestMap);
            List<Article> as = new ArrayList<>();
            Article a = new Article();
            a.setTitle("呵呵！!");
            a.setDescription("点击图文可以跳转到华图首页");
            a.setPicUrl("http://p1.music.126.net/_mEC5ZpzngngbBioF8dm4Q==/109951162973202394.jpg");
            //这里跳转前端验证
            a.setUrl("http://music.163.com/song?id=498040743&userid=84550482");
            as.add(a);
            nm.setArticleCount(as.size());
            nm.setArticles(as);
            return MessageUtil.MessageToXml(nm);
        }
        // TODO 这里还要做特定字符验证，以及正则验证
        TextMessage tm = new TextMessage("暂无验证",requestMap);
        return MessageUtil.MessageToXml(tm);
    }
    @Override
    public String ImageMessageHandler(Map<String, String> requestMap) {
        return null;
    }
}

