package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.MorningReading;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.MorningReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 18:58
 * @Description
 */
@RestController
@RequestMapping("/api/mr")
public class MorningReadingController {
    @Autowired
    private MorningReadingService readingService;

    @PostMapping("insertMorningReading") //@requestBody --> Json
    public Result add(MorningReading morningReading){
        return readingService.add(morningReading)? Result.ok(): Result.build(ResultEnum.INSERT_FAIL);
    }
}
