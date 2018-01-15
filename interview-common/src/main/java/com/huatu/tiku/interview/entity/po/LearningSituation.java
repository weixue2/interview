package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/13 13:55
 * @Description By:学习情况
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_learning_situation")
public class LearningSituation extends BaseEntity{
    //---------通知类型---------
    @Column(columnDefinition="varchar(50) COMMENT '通知类型，说过了'")
    private Long notificationType;
    //---------答题日期---------
    @Column(columnDefinition="date COMMENT '答题日期'")
    private Date answerDate;
    //---------姓名---------
    @Column(columnDefinition="varchar(50) COMMENT '姓名，这都不认识。。'")
    private String name;
    //---------用户ID---------
    @Column(columnDefinition="bigint(20) COMMENT '用户ID'")
    private Long userId;
    //----------练习内容-------------
    @Column(columnDefinition="int(2) COMMENT '练习内容'")
    private Integer practiceContent;
    //------------举止仪态-------------
    @Column(columnDefinition="int(2) COMMENT '举止仪态'")
    private Integer behavior;
    //------------时间把控-------------
    @Column(columnDefinition="int(2) COMMENT '时间把控'")
    private Integer timeControl;
    //------------评价-------------
    @Column(columnDefinition="varchar(50) COMMENT '评价呀评价'")
    private String remark;
}
