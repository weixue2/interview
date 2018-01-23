package com.huatu.tiku.interview.task;

import com.alibaba.fastjson.JSON;
import com.huatu.tiku.interview.entity.dto.ReadingTemp;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Scheduled(fixedDelay = 2 * 10 * 1000)
    public void getReading() throws InterruptedException {
        List<NotificationType> all = readingRepository.findAll();
        for (NotificationType nt : all){

        }

        if(!all.isEmpty()){
            List<ReadingTemp> rts = new ArrayList<>();
            for (NotificationType mr:all){
                rts.add(new ReadingTemp(mr.getId(),mr.getPushTime(),true,mr.getType()));
            }
            String json = JSON.toJSONString(rts);
            System.out.println(rts);
            Thread.sleep(1000*31);
            stringRedisTemplate.opsForValue().set("readings",json);
            stringRedisTemplate.expire("readings",2 * 3600 * 1000, TimeUnit.SECONDS);
        }
    }
}
