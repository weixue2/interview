package com.huatu.tiku.interview.controller.api;

import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.MorningReadingService;
import com.huatu.tiku.interview.util.LogPrint;
import com.huatu.tiku.interview.util.file.HtmlFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 18:58
 * @Description
 */
@RestController
@RequestMapping("/api/mr")
public class MorningReadingApiController {
    // df
    @Autowired
    private MorningReadingService readingService;

    @LogPrint
    @GetMapping
    public Result get(Long id){

        return Result.ok(readingService.get(id));
    }


}
