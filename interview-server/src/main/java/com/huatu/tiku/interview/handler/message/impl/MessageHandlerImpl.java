package com.huatu.tiku.interview.handler.message.impl;

import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.TemplateEnum;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.message.TextMessage;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.handler.message.MessageHandler;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.MessageUtil;
import com.huatu.tiku.interview.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ServletContext servletContext;

    @Override
    public String TextMessageHandler(Map<String, String> requestMap){

        if(!requestMap.get("Content").equals("get")){
            //这个是直接生成String
//            String templateMsgJson = WechatTemplateMsg.getJson(TemplateEnum.No_2,requestMap);
            WechatTemplateMsg templateMsg = new WechatTemplateMsg(TemplateEnum.No_2,requestMap);

            String templateMsgJson = JsonUtil.toJson(templateMsg);
            log.info("accessToken为："+servletContext.getAttribute("accessToken"));
            TemplateMsgResult msgResult = templateMsgService.sendTemplate(
                    "5_7lDDbkX7g-UUIpPS0ZIYc-9MGJhBtXiBYfuaECIZ5a9e4A1J-wxNO73NdxXFTXWHqa5nPj2o9IyU_TB7tphs91VhyOjeeeOSsBHuQMr4RxWMYlCVV0EXFyEL_vQDa_9ZNMyJ2IWCsmU2l5d0DWUaACAUNL",
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

