package com.huatu.tiku.interview.entity.to;

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
public class BaseToMessage {
    // 接收方帐号（收到的OpenID）
    private String ToUserName;
    // 开发者微信号
    private String FromUserName;
    // 消息创建时间 （整型）
    private long CreateTime;
    // 消息类型（text/music/news）
    private String MsgType;
    // 位0x0001被标志时，星标刚收到的消息
    private int FuncFlag;

}
