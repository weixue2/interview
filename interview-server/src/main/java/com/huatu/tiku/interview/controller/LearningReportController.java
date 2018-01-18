package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.entity.vo.request.ReportRequestVO;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.LearningReportService;
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
     * 查询用户学习报告
     */

    @PostMapping(value="report",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result learningReport(@RequestBody ReportRequestVO reportRequestVO){

        log.info("请求参数:{}",reportRequestVO);
        return learningReportService.learningReport(reportRequestVO);
    }



}
