package com.huatu.tiku.interview.task;

import com.alibaba.fastjson.JSON;
import com.huatu.tiku.interview.entity.dto.ReadingTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 10:52
 * @Description
 */
@Component
@Order
public class MorningReadingPushRunner implements CommandLineRunner {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        new Timer().schedule(new RemindTask(), 0,1000);
    }
    class RemindTask extends TimerTask {
        @Override
        public void run() {
            try{

            }catch (Exception e){
                System.out.println("获取时间表出错");
            }
            // TODO 获取时间表
            System.out.println("获取时间表");
            Object o = redisTemplate.opsForValue().get("readings");
            if(o != null){
                List<ReadingTemp> rts =  JSON.parseArray(o.toString(),ReadingTemp.class);
                Date date1 = rts.get(0).getDate();
                Date date2 = new Date();
                Date date3 = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date1);
                System.out.println(cal.getTime());
                System.out.println(cal.get(Calendar.MONTH) + cal.get(Calendar.YEAR));
            }

        }

    }


}
