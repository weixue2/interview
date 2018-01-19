package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.po.LearningSituation;
import com.huatu.tiku.interview.repository.LearningSituationRepository;
import com.huatu.tiku.interview.service.LearningSituationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public LearningSituation findOne(Long id) {
        return learningSituationRepository.findOne(id);
    }

    @Override
    public Boolean save(LearningSituation data) {
        data.setCreator("admin");
        LearningSituation save = learningSituationRepository.save(data);
        return  save == null ?false:true;
    }

    @Override
    public void del(Long id) {
        learningSituationRepository.updateToDel(id);
    }

    @Override
    public List<LearningSituation> findList(String name,Pageable pageRequest) {

        List<LearningSituation> list = learningSituationRepository.findByStatusAndNameLike(1,name, pageRequest);

        return list;
    }

//    WXStatusEnum.Status.NORMAL.getStatus()
    @Override
    public long countByNameLikeStatus(String name) {
        return learningSituationRepository.countByStatusAndNameLike(1,name);
    }
}
