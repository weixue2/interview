package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.po.MorningReading;
import com.huatu.tiku.interview.repository.MorningReadingRepository;
import com.huatu.tiku.interview.service.MorningReadingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 17:17
 * @Description
 */
public class MorningReadingServiceImpl implements MorningReadingService {

    @Autowired
    private MorningReadingRepository morningReadingRepository;

    @Override
    public Boolean add(MorningReading data) {
        return morningReadingRepository.save(data)==null?false:true;
    }

    @Override
    public Boolean update(MorningReading reading) {
        return null;
    }

    @Override
    public void del(Long id) {
    }
}
