package com.huatu.tiku.interview.userHandler.event;

import java.util.Map;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/12 10:05
 * @Modefied By:
 */
public interface EventHandler {

    String subscribeHandler(Map<String, String> requestMap);

    String unsubscribeHandler(Map<String, String> requestMap);

    String signInHandler(Map<String, String> requestMap);


    String eventClick(Map<String, String> requestMap);
}
