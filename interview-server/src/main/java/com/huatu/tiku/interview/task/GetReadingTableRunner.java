package com.huatu.tiku.interview.task;

import com.huatu.tiku.interview.entity.po.MorningReading;
import com.huatu.tiku.interview.service.MorningReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 10:57
 * @Description
 */
//@Component
@Order(10000)
public class GetReadingTableRunner implements CommandLineRunner {

    @Autowired
    private MorningReadingService readingService;

    @Override
    public void run(String... args) throws Exception {
//        readingService.push();
        System.out.println("??????????");
    }
}
