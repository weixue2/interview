package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.po.LearningSituation;
import com.huatu.tiku.interview.repository.LearningSituationRepository;
import com.huatu.tiku.interview.service.LearningSituationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 15:13
 * @Modefied By:
 */
@Service
public class LearningSituationServiceImpl implements LearningSituationService {

    @Autowired
    private LearningSituationRepository learningSituationRepository;
    @Override
    public Boolean add(LearningSituation data) {
        return learningSituationRepository.save(data)==null?false:true;
    }

    @Override
    public Boolean update(LearningSituation data) {
        return null;
    }

    @Override
    public void del(Long id) {
    }
}
