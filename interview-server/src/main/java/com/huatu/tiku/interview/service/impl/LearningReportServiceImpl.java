package com.huatu.tiku.interview.service.impl;

import com.google.gson.Gson;
import com.huatu.common.utils.date.DateFormatUtil;
import com.huatu.tiku.interview.constant.*;
import com.huatu.tiku.interview.entity.po.LearningAdvice;
import com.huatu.tiku.interview.entity.po.LearningReport;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.entity.vo.response.ReportResponseVO;
import com.huatu.tiku.interview.repository.LearningAdviceRepository;
import com.huatu.tiku.interview.repository.LearningReportRepository;
import com.huatu.tiku.interview.repository.LearningSituationRepository;
import com.huatu.tiku.interview.repository.UserRepository;
import com.huatu.tiku.interview.service.LearningReportService;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import static com.huatu.tiku.interview.constant.ReportTypeConstant.DAILY_REPORT;
import static com.huatu.tiku.interview.constant.ReportTypeConstant.TOTAL_REPORT;
import static com.huatu.tiku.interview.constant.UserStatusConstant.EXIST_REPORT;
import static com.huatu.tiku.interview.constant.UserStatusConstant.NO_INFO;
import static com.huatu.tiku.interview.constant.UserStatusConstant.NO_REPORT;

/**
 * Created by x6 on 2018/1/17.
 */
@Service
@Slf4j
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
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Value("${dailyReportURL}")
    private String dailyReportURL;


    /**
     *  生成每日学习报告
     * @return
     */
    @Override
    public Result dailyReport() {

        List<User> userList = userRepository.findByStatus(WXStatusEnum.Status.NORMAL.getStatus());

        if(CollectionUtils.isNotEmpty(userList)){
            for(User user:userList){
                long userId = user.getPhpUserId();

                String openId = user.getOpenId();

                    //查询用户当前已存在的报告数目
                    List<LearningReport> reportList = learningReportRepository.findByOpenIdOrderByDaySortAsc(openId);
                    int daySort = 1;
                    if(CollectionUtils.isNotEmpty(reportList)){
                        daySort = reportList.size() + 1;
                    }
                    if(daySort <= 6){
                        //不是最后一天（只生成每日报告）
                        saveDailyReport(userId,daySort, openId);
                        pushDailyReport(user.getOpenId());
                    }else if(daySort == 7){
                        //最后一天（生成每日报告+总体报告）
                        saveTotalReport(userId, openId);
                        pushTotalReport(user.getOpenId());
                    }
                }

            }

        return Result.ok();
    }


    //推送每日学习报告消息
    public TemplateMsgResult pushDailyReport(String openId)  {
        String accessToken = stringRedisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
//        TemplateEnum.DailyReport.
        WechatTemplateMsg templateMsg = new WechatTemplateMsg(openId, TemplateEnum.DailyReport);

        templateMsg.setUrl(dailyReportURL+openId);
        String templateMsgJson = JsonUtil.toJson(templateMsg);
        TemplateMsgResult result = templateMsgService.sendTemplate(
                accessToken,
                templateMsgJson);
        return result;
    }

    //推送每日学习报告消息
    public TemplateMsgResult pushTotalReport(String openId)  {
        String accessToken = stringRedisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
        WechatTemplateMsg templateMsg = new WechatTemplateMsg(openId, TemplateEnum.TotalReport);

        //根据openId查询用户姓名
        List<User> userList = userRepository.findByOpenIdAndStatus(openId, WXStatusEnum.Status.NORMAL.getStatus());
        String username= "";
        if(CollectionUtils.isNotEmpty(userList)){
            username = userList.get(0).getName();
        }
        TreeMap<String, TreeMap<String, String>> data = templateMsg.getData();
        data.put("keyword1", WechatTemplateMsg.item(username,"#000000"));
        templateMsg.setData(data);
        templateMsg.setUrl(dailyReportURL+openId);
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
    private LearningReport saveDailyReport(long userId,int daySort,String openId) {

        //平均分统计
        List<Object[]> avgList = learningSituationRepository.countTodayAvg(openId);

        //答题数量统计
        List<Object[]> answerCountList = learningSituationRepository.countTodayAnswerCount(openId);
        LearningReport learningReport = buildReport(avgList.get(0), answerCountList);
        learningReport.setDaySort(daySort);

        String format = DateFormatUtil.NORMAL_DAY_FORMAT.format(new Date());
        learningReport.setReportDate(format);
        learningReport.setType(DAILY_REPORT.getCode());
        learningReport.setUserId(userId);
        learningReport.setOpenId(openId);
        learningReport.setStatus(WXStatusEnum.Status.NORMAL.getStatus());
        learningReport = learningReportRepository.save(learningReport);

        return learningReport;
    }

    /**
     * 生成总结报告
     * @param userId
     */
    private LearningReport saveTotalReport(long userId,String openId) {

        //平均分统计
        List<Object[]> avgList = learningSituationRepository.countTotalAvg(openId);
        //答题数量统计
        List<Object[]> answerCountList = learningSituationRepository.countTotalAnswerCount(openId);
        LearningReport learningReport = buildReport(avgList.get(0), answerCountList);
        String format = DateFormatUtil.NORMAL_DAY_FORMAT.format(new Date());

        learningReport.setReportDate(format);
        learningReport.setDaySort(7);
        learningReport.setType(ReportTypeConstant.TOTAL_REPORT.getCode());
        learningReport.setUserId(userId);
        learningReport.setOpenId(openId);
        learningReport.setStatus(WXStatusEnum.Status.NORMAL.getStatus());
        learningReport = learningReportRepository.save(learningReport);

        return learningReport;
    }


    /**
     * 将查询结果封装成report对象
     * @param avgList
     * @param answerCountList
     * @return
     */
    private LearningReport  buildReport(Object[] avgList,List<Object[]> answerCountList){
        LearningReport.LearningReportBuilder builder = LearningReport.builder();
        String avgBehaviorStr = String .format("%.2f",avgList[0]);
        double avgBehavior = Double.parseDouble(avgBehaviorStr);
        String avgLanguageExpressionStr = String .format("%.2f",avgList[1]);
        double avgLanguageExpression = Double.parseDouble(avgLanguageExpressionStr);
        String avgFocusTopicStr = String .format("%.2f",avgList[2]);
        double avgFocusTopic = Double.parseDouble(avgFocusTopicStr);
        String avgIsOrganizedStr = String .format("%.2f",avgList[3]);
        double avgIsOrganized = Double.parseDouble(avgIsOrganizedStr);
        String avgHaveSubstanceStr = String .format("%.2f",avgList[4]);
        double avgHaveSubstance = Double.parseDouble(avgHaveSubstanceStr);

        builder.behavior(avgBehavior)
                .languageExpression(avgLanguageExpression)
                .focusTopic(avgFocusTopic)
                .isOrganized(avgIsOrganized)
                .haveSubstance(avgHaveSubstance);

            int totalCount = 0;
            for(Object[] answerCount:answerCountList){
                Object practiceType = answerCount[0];
                Object count = answerCount[1];
                totalCount = totalCount + Integer.parseInt(count.toString());
                switch (Integer.parseInt(practiceType.toString())) {
                    case 1: {
                        builder.oneAnswerCount(Integer.parseInt(count.toString()));
                        break;
                    }
                    case 2: {
                        builder.twoAnswerCount(Integer.parseInt(count.toString()));
                        break;
                    }
                    case 3: {
                        builder.threeAnswerCount(Integer.parseInt(count.toString()));
                        break;
                    }
                    case 4: {
                        builder.fourAnswerCount(Integer.parseInt(count.toString()));
                        break;
                    }
                    case 5: {
                        builder.fiveAnswerCount(Integer.parseInt(count.toString()));
                        break;
                    }
                    case 6: {
                        builder.sixAnswerCount(Integer.parseInt(count.toString()));
                        break;
                    }
                };
            }

        builder.totalAnswerCount(totalCount);
        LearningReport report = builder.build();
        return report;
    }



    @Override
    public Result learningReport(String openId) {

        //根据openId查询用户id
        User user = userRepository.getUserByOpenIdAndStatus(openId, WXStatusEnum.Status.NORMAL.getStatus());
        if(null == user){
            log.warn("请求参数异常，不存在对应的用户信息。OpenId:{}",openId);
            return Result.build(ResultEnum.OPENID_ERROR);
        }
        //查询用户的所有报告，按天数序号排列
        List<LearningReport> reportList = learningReportRepository.findByOpenIdOrderByDaySortAsc(user.getOpenId());

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
                String reportDate = report.getReportDate();
                List<String> remarkList = learningSituationRepository.findRemarksByOpenIdAndAnswerDateAndStatusOrderByGmtCreateAsc(openId,reportDate);
                reportResponseVO.setRemarkList(remarkList);
            }
            resultList.add(reportResponseVO);
        }
        return Result.ok(resultList);
    }

    @Override
    public Result check(String openId) {
        List<User> userList = userRepository.findByOpenIdAndStatus(openId, WXStatusEnum.Status.NORMAL.getStatus());

        String content = "";
        if(CollectionUtils.isEmpty(userList)){
            log.info("校验用户状态 抱歉，您尚未填写个人信息，无法核实您的学员身份~");
            content = "校验用户状态 抱歉，您尚未填写个人信息，无法核实您的学员身份~";
//            pushText(openId,content);
            return Result.ok(NO_INFO.getStatus());

        }else{
            //判断报告是否已经生成
            List<LearningReport> learningReports = learningReportRepository.findByOpenIdOrderByIdAsc(openId);
            if(CollectionUtils.isEmpty(learningReports)){
                log.info("学习报告尚未生成~");
                content = "学习报告尚未生成~";
//                pushText(openId,content);
                return Result.ok(NO_REPORT.getStatus());
            }
        }
        return Result.ok(EXIST_REPORT.getStatus());
    }

    public void pushText(String openId,String content) {

        String msg = "{\"touser\":"+openId+", \"msgtype\":\"text\",\"text\":{ \"content\": "+content+"\"} }";
        String jsonString = new Gson().toJson(msg).toString();
        // 调用接口获取access_token
        String at = redisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN);
        String requestUrl = WeChatUrlConstant.MESSAGE_MANY_URL.replace(WeChatUrlConstant.ACCESS_TOKEN, at);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<>(msg, headers);

        String result =  restTemplate.postForObject(requestUrl,formEntity,String.class);
        log.info("result:"+result);
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
