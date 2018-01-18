package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.NotificationType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author jbzm
 * @Date Create on 2018/1/18 17:25
 */
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {
    List<NotificationType> findByBizStatusAndStatus(Sort id, int bizSatus, int status);
}
