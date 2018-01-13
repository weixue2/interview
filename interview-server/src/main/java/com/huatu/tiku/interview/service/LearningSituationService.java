package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.LearningSituation;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 15:13
 * @Modefied By:
 */
public interface LearningSituationService {

    Boolean add(LearningSituation data);
    // 这个其实是多余的，我跟你讲
    Boolean update(LearningSituation data);
    void del(Long id);

}
