package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.NotificationType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author jbzm
 * @Date Create on 2018/1/18 17:25
 */
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long>  , JpaSpecificationExecutor<NotificationType> {
    List<NotificationType> findByBizStatusAndStatus(Sort id, int bizSatus, int status);

    @Query(value = "select n from NotificationType n where n.title like %?1% ")
    List<NotificationType> findByTitleLimit(String title, PageRequest pageable);

}
