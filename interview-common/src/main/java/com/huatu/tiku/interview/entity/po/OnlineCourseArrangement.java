package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 15:09
 * @Description 线上课程安排
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_online_course_arrangement")
public class OnlineCourseArrangement extends BaseEntity{
    @Column(columnDefinition="varchar(50) default '线上课程安排' COMMENT '通知类型'")
    private String type;

    @Column(columnDefinition="varchar(50) COMMENT '标题'")
    private String title;

    @Column(columnDefinition="varchar(150) COMMENT '图片地址'")
    private String imageUrl;
}
