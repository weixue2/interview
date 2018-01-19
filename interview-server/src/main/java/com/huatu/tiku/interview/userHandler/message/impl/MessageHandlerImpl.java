package com.huatu.tiku.interview.userHandler.message.impl;

import com.huatu.tiku.interview.constant.TemplateEnum;
import com.huatu.tiku.interview.entity.message.TextMessage;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.userHandler.message.MessageHandler;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.MessageUtil;
import com.huatu.tiku.interview.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
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
    private ServletContext servletContext;

    @Override
    public String TextMessageHandler(Map<String, String> requestMap){

        if(!requestMap.get("Content").equals("get")){
            //这个是直接生成String
//            String templateMsgJson = WechatTemplateMsg.getJson(TemplateEnum.No_2,requestMap);
            WechatTemplateMsg templateMsg = new WechatTemplateMsg(TemplateEnum.No_2,requestMap);

            String templateMsgJson = JsonUtil.toJson(templateMsg);
            TemplateMsgResult msgResult = templateMsgService.sendTemplate(
                    "6_Du950DMB6FT-VeZGW8Pvftu9nc6kTk8WhHy_CAWJ12htL_x5ETtxUz270aZ1Ww8BbSy0uLQt5a8NwgKQOAwBS4b2JXBxhm3EzxcW6usp6lcOxxCLPJF0t_WOvBjiphdl8x8wGwzdfZx5o9MCTMWgAGAATD",
                    templateMsgJson);
            return null;
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

