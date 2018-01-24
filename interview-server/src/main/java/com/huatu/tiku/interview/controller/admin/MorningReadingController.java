package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.MorningReadingService;
import com.huatu.tiku.interview.util.LogPrint;
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


    @LogPrint
    @PostMapping //@requestBody --> Json
    public Result add(@RequestBody NotificationType morningReading){
        morningReading.setStatus(1);
        morningReading.setType(2);
        Long id = readingService.add(morningReading);
        return  Result.ok(id);
    }

    @LogPrint
    @GetMapping
    public Result get(Long id){

        return Result.ok(readingService.get(id));
    }
}
