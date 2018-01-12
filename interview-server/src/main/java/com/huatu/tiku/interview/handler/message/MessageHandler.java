package com.huatu.tiku.interview.handler.message;

import java.util.Map;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/12 9:52
 * @Modefied By:
 */
public interface MessageHandler {
    public String TextMessageHandler(Map<String, String> requestMap);
    public String ImageMessageHandler(Map<String, String> requestMap);
}
