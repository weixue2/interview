package com.huatu.tiku.interview.entity.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhouwei
 * @Description: 文本消息
 * @create 2018-01-04 下午12:02
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TextFromMessage extends BaseFromMessage {
    private String content;

}
