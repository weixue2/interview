package com.huatu.tiku.interview.event;

import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.to.NewsToMessage;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouwei
 * @Description: 事件处理类
 * @create 2018-01-06 下午4:01
 **/
@Component
public class EventHandler {
    @Autowired
    UserService userService;

    /**
     * @param fromUserName 发送者OpenId
     * @param newsMessage
     * @param respMessage
     * @return
     */
    public final String subscribeEvent(String fromUserName, NewsToMessage newsMessage, String respMessage) {
            //创建用户，记录openId
        userService.createUser(fromUserName);

        List<Article> articleList = new ArrayList<Article>();
        //测试单图文回复
        Article article = new Article();
        article.setTitle("谢谢您的关注！");
        // 图文消息中可以使用QQ表情、符号表情
        article.setDescription("点击图文可以跳转到华图首页");
        // 将图片置为空
        article.setPicUrl("http://www.sinaimg.cn/dy/slidenews/31_img/2016_38/28380_733695_698372.jpg");
        article.setUrl("http://v.huatu.com");
        articleList.add(article);
        newsMessage.setArticleCount(articleList.size());
        newsMessage.setArticles(articleList);
        respMessage = MessageUtil.newsMessageToXml(newsMessage);
        return respMessage;
    }
}
