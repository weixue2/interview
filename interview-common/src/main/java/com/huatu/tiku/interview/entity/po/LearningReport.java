package com.huatu.tiku.interview.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by x6 on 2018/1/17.
 *
 * 学习报告
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="t_learning_report")
public class LearningReport extends BaseEntity  implements Serializable {
    //第几天
    private Integer daySort;

    //用户id
    private Long userId;
    //---------openId---------
    private String openId;

    //统计日期
    private String reportDate;

    //总作答题目数
    private Integer totalAnswerCount;

    // 1自我认知  2工作实务   3策划组织  4综合分析   5材料题与特殊题型   6套题演练
    // 1自我认知  作答题目数
    private int oneAnswerCount;
    //2工作实务  作答题目数
    private int twoAnswerCount;
    //3策划组织  作答题目数
    private int threeAnswerCount;
    //4综合分析  作答题目数
    private int fourAnswerCount;
    //5材料题与特殊题型  作答题目数
    private int fiveAnswerCount;
    //6套题演练  作答题目数
    private int sixAnswerCount;

    //------------举止仪态-------------
    private Double behavior;
    //------------语言表达-------------
    private Double languageExpression;
    //------------是否精准扣题-------------
    private Double focusTopic;
    //------------是否条理清晰-------------
    private Double isOrganized;
    //------------是否言之有物-------------
    private Double haveSubstance;


    //------------报告类型（1总计报告  0单日报告）-------------
    private int type;







}
