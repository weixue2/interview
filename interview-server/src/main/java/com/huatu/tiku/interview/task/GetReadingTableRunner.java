package com.huatu.tiku.interview.task;

import com.alibaba.fastjson.JSON;
import com.huatu.tiku.interview.entity.dto.ReadingTemp;
import com.huatu.tiku.interview.entity.po.MorningReading;
import com.huatu.tiku.interview.repository.MorningReadingRepository;
import com.huatu.tiku.interview.service.MorningReadingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 10:57
 * @Description
 */
@Component
@Slf4j
public class GetReadingTableRunner{

    @Autowired
    MorningReadingRepository readingRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //获取晨读。。
    @Scheduled(fixedDelay = 2 * 3600 * 1000)
    public void getReading(){
        List<MorningReading> all = readingRepository.findAll();
        if(!all.isEmpty()){
            List<ReadingTemp> rts = new ArrayList<>();
            for (MorningReading mr:all){
                rts.add(new ReadingTemp(mr.getId(),mr.getPushTime(),true));
            }
            String json = JSON.toJSONString(rts);
            stringRedisTemplate.opsForValue().set("readings", json);
//            redisTemplate.opsForValue().set("readings",rts);
//            httpSession.setAttribute("test1996","tesst1996");
        }
    }
}
