package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.entity.TestEntity;
import com.huatu.tiku.interview.entity.po.LearningSituation;
import com.huatu.tiku.interview.entity.result.ReqResult;
import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.service.LearningSituationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 14:34
 * @Modefied By:
 */
@RestController
@RequestMapping("/api/ls")
public class ListController {

    @Autowired
    private LearningSituationService learningSituationService;

    @PostMapping(value = "insertLearningSituation")
    public ReqResult addLearningSituation(LearningSituation learningSituation){
        System.out.println(learningSituation);
        return learningSituationService.insertLearningSituation(learningSituation)? ReqResult.ok():ReqResult.build(ResultEnum.insertFail);
    }



}
