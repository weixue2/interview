package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.MorningReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 18:58
 * @Description
 */
@RestController
@RequestMapping("/end/mr")
public class MorningReadingController {
    @Autowired
    private MorningReadingService readingService;

    @PostMapping //@requestBody --> Json
    public Result add(@RequestBody NotificationType morningReading){
        morningReading.setType(2);
        return readingService.add(morningReading)? Result.ok(): Result.build(ResultEnum.INSERT_FAIL);
    }

    @GetMapping
    public Result get(Long id){

        return Result.ok(readingService.get(id));
    }
}
