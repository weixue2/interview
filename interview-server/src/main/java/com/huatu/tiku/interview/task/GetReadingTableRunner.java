package com.huatu.tiku.interview.task;

import com.alibaba.fastjson.JSON;
import com.huatu.tiku.interview.entity.dto.ReadingTemp;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
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
    NotificationTypeRepository readingRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //获取晨读。。
    @Scheduled(fixedDelay = 2 * 3600 * 1000)
    public void getReading() throws InterruptedException {
        List<NotificationType> all = readingRepository.findAll();
        System.out.println("通知有几个？"+all.size());
        for (NotificationType nt : all){

        }

        if(!all.isEmpty()){
            List<ReadingTemp> rts = new ArrayList<>();
            for (NotificationType mr:all){
                rts.add(new ReadingTemp(mr.getId(),mr.getPushTime(),true));
            }
            String json = JSON.toJSONString(rts);
            Thread.sleep(1000*60);
            stringRedisTemplate.opsForValue().set("readings", json);
        }
    }
}
