package com.huatu.tiku.interview.entity.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouwei
 * @Description: 消息基类（普通用户 -> 公众帐号）
 * @create 2018-01-04 下午12:02
 **/
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BaseFromMessage {
    // 开发者微信号
    private String ToUserName;
    // 发送方帐号（一个OpenID）
    private String FromUserName;
    // 消息创建时间 （整型）
    private long CreateTime;
    // 消息类型（text/image/location/link）
    private String MsgType;
    // 消息id，64位整型
    private long MsgId;

}
