package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 17:32
 * @Description
 */
public interface NotificationTypeRepository extends JpaRepository<NotificationType,Long> {
}
