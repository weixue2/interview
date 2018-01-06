package com.huatu.tiku.interview.event;

import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.to.NewsToMessage;
import com.huatu.tiku.interview.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-06 下午4:01
 **/
public class EventHandler {
    public static final String subscribeEvent(NewsToMessage newsMessage, String respMessage){
        List<Article> articleList = new ArrayList<Article>();
        //测试单图文回复
        Article article = new Article();
        article.setTitle("谢谢您的关注！");
        // 图文消息中可以使用QQ表情、符号表情
        article.setDescription("点击图文可以跳转到百度首页");
        // 将图片置为空
        article.setPicUrl("http://www.sinaimg.cn/dy/slidenews/31_img/2016_38/28380_733695_698372.jpg");
        article.setUrl("http://www.baidu.com");
        articleList.add(article);
        newsMessage.setArticleCount(articleList.size());
        newsMessage.setArticles(articleList);
        respMessage = MessageUtil.newsMessageToXml(newsMessage);
             return respMessage;
    }
}
