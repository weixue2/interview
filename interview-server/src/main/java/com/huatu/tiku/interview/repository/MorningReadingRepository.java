package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.MorningReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 16:59
 * @Description
 */
@Repository
public interface MorningReadingRepository  extends JpaRepository<MorningReading,Long>{
}
