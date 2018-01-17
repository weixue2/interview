package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author ZhenYangG
 * @Date Created in 2018/1/13 15:09
 * @Description 线上课程安排
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Table(name="t_online_course_arrangement")
public class OnlineCourseArrangement extends NotificationType {
    /**
     * 名字
     */
    private String title;
    /**
     * 图片url
     */
    private String imageUrl;
    /**
     * 微信认证图片id
     */
    private Integer wxImageId;

}
