package com.huatu.tiku.interview.controller.api;

import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.service.LearningReportService;
import com.huatu.tiku.interview.util.LogPrint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by x6 on 2018/1/17.
 *
 *  学习报告相关接口
 */
@RestController
@RequestMapping("/api/lr")
@Slf4j
public class LearningReportController {

    @Autowired
    private LearningReportService learningReportService;


    /**
     * 生成用户学习报告
     */
    @LogPrint
    @PostMapping(value="daily",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result dailyReport(){
        return learningReportService.dailyReport();
    }



    /**
     * 查询用户学习报告
     */
    @LogPrint
    @GetMapping(value="report",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result learningReport(@RequestParam String openId){

        log.info("请求参数openId:{}",openId);
        return learningReportService.learningReport(openId);
    }

    /**
     * 校验用户当前状态
     */
    @LogPrint
    @GetMapping(value="check",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result checkuser(@RequestParam String openId){

        log.info("请求参数openId:{}",openId);
        return learningReportService.check(openId);
    }



    /**
     * 推送用户学习报告
     */
    @LogPrint
    @PostMapping(value="push/{openId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TemplateMsgResult push(@PathVariable String openId){
        return learningReportService. pushDailyReport(openId);
    }


    /**
     * 推送用户学习历程
     */
    @LogPrint
    @PostMapping(value="pushTotal/{openId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TemplateMsgResult pushTotal(@PathVariable String openId){
        return learningReportService. pushTotalReport(openId);
    }



}
