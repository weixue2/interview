package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 13:00
 * @Modefied By:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="t_learning_situation")
@Entity
public class LearningSituation extends  BaseEntity{

    //---------通知类型---------
    private Long notificationType;
    //---------答题日期---------
    private Date answerDate;
    //---------姓名---------
    private String name;
    //---------用户ID---------
    private Long userId;
    //---------题号---------
    private Long questionNumber;
    //---------题型---------
    private Long questionType;
    //---------行为举止---------
    private Long behaviour;
    //---------面部表情---------
    private Long countenance;
    //---------语速---------
    private Long speakSpeed;
    //---------语调---------
    private Long intonation;
    //---------音量---------
    private Long soundVolume;
    //---------是否跑题---------
    private Boolean isDigress;
    //---------是否清晰---------
    private Boolean isClearly;
    //---------是否言之有物---------
    private Boolean isDryCargo;
    //---------是否内容合理---------
    private Boolean isReasonable;
    //-------------评语-------------
    private String remark;
}
