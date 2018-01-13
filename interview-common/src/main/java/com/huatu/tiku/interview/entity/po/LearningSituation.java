package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/13 13:55
 * @Description By:
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_learning_situation")
public class LearningSituation extends BaseEntity{
    //---------通知类型---------
    private Long notificationType;
    //---------答题日期---------
    private Date answerDate;
    //---------姓名---------
    private String name;
    //---------用户ID---------
    private Long userId;
    //----------练习内容-------------
    private String practiceContent;
    //------------举止仪态-------------
    private Integer behavior;
    //------------时间把控-------------
    private Integer timeControl;
    //------------评价-------------
    private String remark;
}
