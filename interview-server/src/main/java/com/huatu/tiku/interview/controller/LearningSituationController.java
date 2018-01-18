package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.LearningSituation;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.LearningSituationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 14:34
 * @Modefied By: 学习情况
 */
@Slf4j
@RestController
@RequestMapping("/api/ls")
public class LearningSituationController {

    @Autowired
    private LearningSituationService learningSituationService;

    /**
     * 新增学员学习情况
     * @param learningSituation
     * @return
     */
    @PostMapping(value="",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result add(@RequestBody LearningSituation learningSituation){
        log.info("请求参数learningSituation：{}",learningSituation);
        return learningSituationService.save(learningSituation)? Result.ok(): Result.build(ResultEnum.INSERT_FAIL);
    }


    @PutMapping(value="",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result del(Long id){
        learningSituationService.del(id);
        return Result.ok();
    }

    @PutMapping(value="learnSituation",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result update(LearningSituation learningSituation){
        learningSituation = learningSituationService.findOne(learningSituation.getId());
        if (learningSituation == null){
            return Result.build(ResultEnum.UPDATE_FAIL);
        }else{
            return learningSituationService.save(learningSituation)? Result.ok(): Result.build(ResultEnum.UPDATE_FAIL);
        }
    }


}
