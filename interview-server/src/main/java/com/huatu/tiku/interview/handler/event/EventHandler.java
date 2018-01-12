package com.huatu.tiku.interview.handler.event;

import com.huatu.tiku.interview.entity.message.NewsMessage;

import java.util.Map;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/12 10:05
 * @Modefied By:
 */
public interface EventHandler {

    public String subscribeHandler(Map<String, String> requestMap);
    public String unsubscribeHandler(Map<String, String> requestMap);
}
