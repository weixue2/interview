package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;
import com.huatu.tiku.interview.repository.OnlineCourseArrangementRepository;
import com.huatu.tiku.interview.service.OnlineCourseArrangementService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 17:16
 * @Description
 */
public class OnlineCourseArrangementServiceImpl implements OnlineCourseArrangementService {

    @Autowired
    private OnlineCourseArrangementRepository onlineCourseArrangementRepository;
    @Override
    public Boolean add(OnlineCourseArrangement data) {
        return onlineCourseArrangementRepository.save(data)==null?false:true;
    }

    @Override
    public Boolean update(OnlineCourseArrangement arrangement) {
        return null;
    }

    @Override
    public void del(Long id) {

    }
}
