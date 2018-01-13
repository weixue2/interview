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
    @ElementCollection(targetClass = Date.class)
    @CollectionTable(name = "morning_reading_push_time",joinColumns = @JoinColumn(name = "morning_reading_id"))
    @MapKeyClass(String.class)
    @MapKeyColumn(name = "week")
    @Column(name = "time")
    private Map<String,Date> pushTime;

    private String title;

    @Column(length = 16777216)
    private String richText;

}
