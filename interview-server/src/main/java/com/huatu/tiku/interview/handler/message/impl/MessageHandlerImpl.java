package com.huatu.tiku.interview.handler.message.impl;

import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.message.TextMessage;
import com.huatu.tiku.interview.handler.message.MessageHandler;
import com.huatu.tiku.interview.util.MessageUtil;
import org.springframework.stereotype.Component;

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
public class MessageHandlerImpl implements MessageHandler {


    @Override
    public String TextMessageHandler(Map<String, String> requestMap){

        if(requestMap.get("Content").equals("get")){

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

