package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;

/**
 * Created by x6 on 2018/1/17.
 * 学习报告相关接口
 */
public interface LearningReportService {
    Result dailyReport();

    Result learningReport(String openId);

    Result check(String openId);

    TemplateMsgResult pushDailyReport(String openId);
}
