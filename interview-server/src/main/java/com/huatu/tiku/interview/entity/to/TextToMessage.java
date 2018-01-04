package com.huatu.tiku.interview.entity.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-04 下午1:43
 **/
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TextToMessage extends BaseToMessage {
    // 回复的消息内容
    private String Content;
}
