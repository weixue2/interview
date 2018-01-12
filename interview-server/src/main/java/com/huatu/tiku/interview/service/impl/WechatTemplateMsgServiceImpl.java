package com.huatu.tiku.interview.service.impl;


import com.huatu.tiku.interview.config.WechatConfig;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.HttpReqUtil;
import com.huatu.tiku.interview.util.json.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/12 16:30
 * @Description
 */
@Service
public class WechatTemplateMsgServiceImpl implements WechatTemplateMsgService {

    /**
     * 发送模板消息
     * @param accessToken
     * @param data
     * @return 状态
     */
    @Override
    public TemplateMsgResult sendTemplate(String accessToken, String data) {
        TemplateMsgResult templateMsgResult = null;
        TreeMap<String,String> params = new TreeMap<String,String>();
        params.put("access_token", accessToken);
        String result = HttpReqUtil.HttpsDefaultExecute(HttpReqUtil.POST_METHOD, WechatConfig.SEND_TEMPLATE_MESSAGE, params, data);
        templateMsgResult = JsonUtil.fromJson(result, TemplateMsgResult.class);
        return templateMsgResult;
    }
}