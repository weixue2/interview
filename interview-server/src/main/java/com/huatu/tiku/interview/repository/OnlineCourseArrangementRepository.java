package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 17:00
 * @Description
 */
@Repository
public interface OnlineCourseArrangementRepository extends JpaRepository<OnlineCourseArrangement, Long> {
    List<OnlineCourseArrangement> findByBizStatusAndStatus(Sort updateTimestamp, WXStatusEnum.BizStatus normal, WXStatusEnum.Status online);
}
