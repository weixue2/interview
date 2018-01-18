package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.po.MorningReading;
import com.huatu.tiku.interview.repository.MorningReadingRepository;
import com.huatu.tiku.interview.service.MorningReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 17:17
 * @Description
 */
@Service
public class MorningReadingServiceImpl implements MorningReadingService {

    @Autowired
    private HttpSession httpSession;

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

    @Override
    public List<MorningReading> findAll() {
        Sort sort = new Sort("pushTime","desc");
        return morningReadingRepository.findAll(sort);
    }

    public void  push(){
        Sort sort = new Sort("pushTime","desc");
        List<MorningReading> readings =  morningReadingRepository.findAll(sort);
        System.out.println(readings.size());
    }
}
