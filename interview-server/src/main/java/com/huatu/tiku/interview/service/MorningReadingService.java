package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.MorningReading;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 17:12
 * @Description 晨读
 */
public interface MorningReadingService {
    Boolean add(MorningReading reading);
    // 这个其实是多余的，我跟你讲
    Boolean update(MorningReading reading);
    Boolean del(Long id);
}
