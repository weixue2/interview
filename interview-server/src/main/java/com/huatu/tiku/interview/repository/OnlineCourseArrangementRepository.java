package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author jbzm
 * @Date Create on 2018/1/17 17:21
 */
@Repository
public interface OnlineCourseArrangementRepository extends JpaRepository<OnlineCourseArrangement, Long> {

    List<OnlineCourseArrangement> findByBizStatusAndStatus(Sort id, int bizSatus, int status);
}
