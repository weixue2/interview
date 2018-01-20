package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.constant.ReportTypeConstant;
import com.huatu.tiku.interview.constant.TemplateEnum;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.po.LearningAdvice;
import com.huatu.tiku.interview.entity.po.LearningReport;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.entity.vo.request.ReportRequestVO;
import com.huatu.tiku.interview.entity.vo.response.ReportResponseVO;
import com.huatu.tiku.interview.repository.LearningAdviceRepository;
import com.huatu.tiku.interview.repository.LearningReportRepository;
import com.huatu.tiku.interview.repository.LearningSituationRepository;
import com.huatu.tiku.interview.repository.UserRepository;
import com.huatu.tiku.interview.service.LearningReportService;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.json.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.huatu.tiku.interview.constant.ReportTypeConstant.DAILY_REPORT;
import static com.huatu.tiku.interview.constant.ReportTypeConstant.TOTAL_REPORT;
import static com.huatu.tiku.interview.constant.ResultEnum.PARAMETER_NULL_ERROR;

/**
 * Created by x6 on 2018/1/17.
 */
@Service
public class LearningReportServiceImpl  implements LearningReportService {
    @Autowired
    private LearningReportRepository learningReportRepository;
    @Autowired
    private LearningAdviceRepository learningAdviceRepository;
    @Autowired
    private LearningSituationRepository learningSituationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    WechatTemplateMsgService templateMsgService;

    /**
     *  生成每日学习报告
     * @return
     */
    @Override
    public Result dailyReport() {

        List<User> userList = userRepository.findAll();

        if(CollectionUtils.isNotEmpty(userList)){
            for(User user:userList){
                long userId = user.getId();
                //查询用户当前已存在的报告数目
                List<LearningReport> reportList = learningReportRepository.findByUserIdOrderByIdAsc(userId);
                int daySort = 1;
                if(CollectionUtils.isNotEmpty(reportList)){
                    daySort = reportList.size() + 1;
                }
                if(daySort <= 6){
                    //不是最后一天（只生成每日报告）
                    saveDailyReport(userId,daySort);
                    pushDailyReport(user.getOpenId());
                }else if(daySort == 7){
                    //最后一天（生成每日报告+总体报告）
                    saveTotalReport(userId);
                    pushTotalReport(user.getOpenId());
                }
            }
        }
        return Result.ok();
    }


    //推送每日学习报告消息
    private TemplateMsgResult pushDailyReport(String openId)  {
        String accessToken = stringRedisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
        WechatTemplateMsg templateMsg = new WechatTemplateMsg(openId, TemplateEnum.DailyReport);
        String templateMsgJson = JsonUtil.toJson(templateMsg);
        TemplateMsgResult result = templateMsgService.sendTemplate(
                accessToken,
                templateMsgJson);
        return result;
    }

    //推送每日学习报告消息
    private TemplateMsgResult pushTotalReport(String openId)  {
        String accessToken = stringRedisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
        WechatTemplateMsg templateMsg = new WechatTemplateMsg(openId, TemplateEnum.TotalReport);
        String templateMsgJson = JsonUtil.toJson(templateMsg);
        TemplateMsgResult result = templateMsgService.sendTemplate(
                accessToken,
                templateMsgJson);
        return result;
    }


    /**
     * 生成每日报告
     * @param userId
     */
    private LearningReport saveDailyReport(long userId,int daySort) {

        //平均分统计
        List<Double> avgList = learningSituationRepository.countTodayAvg(userId);

       //答题数量统计
        List<List<Integer>> answerCountList = learningSituationRepository.countTodayAnswerCount(userId);
        LearningReport learningReport = buildReport(avgList, answerCountList);
        learningReport.setDaySort(daySort);
        learningReport.setReportDate(new Date());
        learningReport.setType(DAILY_REPORT.getCode());
        learningReport.setUserId(userId);
        learningReport = learningReportRepository.save(learningReport);

        return learningReport;
    }

    /**
     * 生成总结报告
     * @param userId
     */
    private LearningReport saveTotalReport(long userId) {

        //平均分统计
        List<Double> avgList = learningSituationRepository.countTotalAvg(userId);
        //答题数量统计
        List<List<Integer>> answerCountList = learningSituationRepository.countTotalAnswerCount(userId);
        LearningReport learningReport = buildReport(avgList, answerCountList);

        learningReport.setDaySort(8);
        learningReport.setReportDate(new Date());
        learningReport.setType(ReportTypeConstant.TOTAL_REPORT.getCode());
        learningReport.setUserId(userId);

        learningReport = learningReportRepository.save(learningReport);
        return learningReport;
    }


    /**
     * 将查询结果封装成report对象
     * @param avgList
     * @param answerCountList
     * @return
     */
    private LearningReport  buildReport(List<Double> avgList,List<List<Integer>> answerCountList){
        LearningReport.LearningReportBuilder builder = LearningReport.builder();
        Double avgBehavior = avgList.get(0);
        Double avgLanguageExpression = avgList.get(1);
        Double avgFocusTopic = avgList.get(2);
        Double avgIsOrganized = avgList.get(3);
        Double avgHaveSubstance = avgList.get(4);
        builder.behavior(avgBehavior)
                .languageExpression(avgLanguageExpression)
                .focusTopic(avgFocusTopic)
                .isOrganized(avgIsOrganized)
                .haveSubstance(avgHaveSubstance);

        if(CollectionUtils.isNotEmpty(answerCountList)){
            for(List<Integer> answerCount:answerCountList){
                Integer practiceType = answerCount.get(0);
                Integer count = answerCount.get(1);
                switch (practiceType) {
                    case 1: {
                        builder.oneAnswerCount(count);
                        break;
                    }
                    case 2: {
                        builder.twoAnswerCount(count);
                        break;
                    }
                    case 3: {
                        builder.threeAnswerCount(count);
                        break;
                    }
                    case 4: {
                        builder.fourAnswerCount(count);
                        break;
                    }
                    case 5: {
                        builder.fiveAnswerCount(count);
                        break;
                    }
                    case 6: {
                        builder.sixAnswerCount(count);
                        break;
                    }
                };
            }
        }
        LearningReport report = builder.build();
        return report;
    }



    @Override
    public Result learningReport(ReportRequestVO reportRequestVO) {
        if(null == reportRequestVO){
            return Result.build(PARAMETER_NULL_ERROR);
        }
        //用户id
        Long userId = reportRequestVO.getUserId();
        //查询用户的所有报告，按天数序号排列
        List<LearningReport> reportList = learningReportRepository.findByUserIdOrderByIdAsc(userId);

        List<ReportResponseVO> resultList = new LinkedList<>();
        for(LearningReport report: reportList){
            ReportResponseVO reportResponseVO = new ReportResponseVO();
            BeanUtils.copyProperties(report,reportResponseVO);
            if( TOTAL_REPORT.getCode() == report.getType()){
                //根据成绩给出建议
                // 举止仪态
                Double behavior = report.getBehavior();
                reportResponseVO.setBehaviorAdvice(getAdvice(behavior,1));

                Double languageExpression = report.getLanguageExpression();
                reportResponseVO.setLanguageExpressionAdvice(getAdvice(languageExpression,2));

                Double focusTopic = report.getFocusTopic();
                reportResponseVO.setFocusTopicAdvice(getAdvice(focusTopic,3));

                Double isOrganized = report.getIsOrganized();
                reportResponseVO.setIsOrganizedAdvice(getAdvice(isOrganized,4));

                Double haveSubstance = report.getHaveSubstance();
                reportResponseVO.setHaveSubstanceAdvice(getAdvice(haveSubstance,5));
            }else if( DAILY_REPORT.getCode() == report.getType()){
                //老师评语(查询当天学员所有的学习记录)
                Date reportDate = report.getReportDate();
                List<String> remarkList = learningSituationRepository.findRemarksByAnswerDateAndStatusOrderByGmtCreateAsc(reportDate);
                reportResponseVO.setRemarkList(remarkList);
            }
            resultList.add(reportResponseVO);
        }
        return Result.build(100,"查询成功",resultList);
    }


    /**
     * 根据成绩查询建议
     * @param score
     * @param type
     * @return
     */
    private String getAdvice(Double score,Integer type){
        int level = 0;
        String advice = "";
        //根据成绩判断分数等级
        if(score >= 0 && score < 2.5){
            level = 3;
        }else if(score >= 2.5 && score < 4){
            level = 2;
        }else if(score >= 4 && score <= 5){
            level = 1;
        }

        List<LearningAdvice> adviceList = learningAdviceRepository.findByTypeAndLevel(type, level);
        if(CollectionUtils.isNotEmpty(adviceList)){
            advice = adviceList.get(0).getContent();
        }
        return advice;
    }


}
