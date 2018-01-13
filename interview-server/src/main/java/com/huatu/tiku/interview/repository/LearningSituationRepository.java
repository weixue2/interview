package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.LearningSituation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/11 16:06
 * @Modefied By:
 */
@Repository
public interface LearningSituationRepository extends JpaRepository<LearningSituation, Long> {

}
