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
        System.out.println(all.get(0).getType());
        System.out.println("通知有几个？"+all.size());
        for (NotificationType nt : all){

        }

        if(!all.isEmpty()){
            List<ReadingTemp> rts = new ArrayList<>();
            for (NotificationType mr:all){
                rts.add(new ReadingTemp(mr.getId(),mr.getPushTime(),true,mr.getType()));
            }
            String json = JSON.toJSONString(rts);
            System.out.println(rts);
            System.out.println("这森"+json);
//            Thread.sleep(1000*31);
            stringRedisTemplate.opsForValue().set("readings",json);
//            Object o = stringRedisTemplate.opsForValue().get("readings");
//            System.out.println("过去"+o.toString());
//            List<ReadingTemp> list = (List<ReadingTemp>)o;
//            System.out.println(list.size());
        }
    }
}
