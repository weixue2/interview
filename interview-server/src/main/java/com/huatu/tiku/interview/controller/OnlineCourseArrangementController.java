package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;
import com.huatu.tiku.interview.entity.result.ReqResult;
import com.huatu.tiku.interview.service.OnlineCourseArrangementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 18:59
 * @Description
 */
@RestController
public class OnlineCourseArrangementController {

    @Autowired
    private OnlineCourseArrangementService arrangementService;

    @PostMapping("insertOnlineCourseArrangement") //@requestBody --> Json
    public ReqResult add(OnlineCourseArrangement onlineCourseArrangement){
        return arrangementService.add(onlineCourseArrangement)? ReqResult.ok():ReqResult.build(ResultEnum.insertFail);
    }
}
