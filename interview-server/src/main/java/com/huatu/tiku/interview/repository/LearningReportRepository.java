package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.LearningReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by x6 on 2018/1/17.
 */
public interface LearningReportRepository extends JpaRepository<LearningReport, Long> {

    //查询用户下的所有报告
    List<LearningReport> findByUserIdOrderByIdAsc(Long userId);

    //查询用户下的所有报告
    List<LearningReport> findByOpenIdOrderByIdAsc(String openId);
}
