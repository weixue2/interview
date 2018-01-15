package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 15:11
 * @Description 鸡汤及晨读内容
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_morning_reading")
public class MorningReading extends BaseEntity{
    //产品真滴是误导人，传一个时间就传一个时间呗，你非得跟我解释每周七天推送时间是不同的云云，我靠，一个简单的时间字段你给我解释出花来
//    @ElementCollection(targetClass = Date.class)
//    @CollectionTable(name = "morning_reading_push_time",joinColumns = @JoinColumn(name = "morning_reading_id"))
//    @MapKeyClass(String.class)
//    @MapKeyColumn(name = "week")
//    @Column(name = "time")
//    private Map<String,Date> pushTime;
    private Date pushTime;

    @Column(columnDefinition="varchar(50) default '鸡汤及晨读内容' COMMENT '肯定是通知类型啊，写死的'")
    private String type;

    @Column(columnDefinition="varchar(50) COMMENT '标题啊'")
    private String title;

    @Column(length = 16777216,columnDefinition="varchar(50) COMMENT '富文本。。很多很多文本'")
    private String richText;

}
