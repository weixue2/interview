package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.NotificationType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author jbzm
 * @Date Create on 2018/1/18 17:25
 */
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long>  , JpaSpecificationExecutor<NotificationType> {
    List<NotificationType> findByBizStatusAndStatus(Sort id, int bizSatus, int status);

    @Query(value = "select n from NotificationType n where n.title like %?1% ")
    List<NotificationType> findByTitleLimit(String title, PageRequest pageable);

    @Transactional
    @Modifying
    @Query("update NotificationType ls set ls.status=-1 where ls.id=?1")
    int updateToDel(Long id);

    @Query("select n from NotificationType n where n.pushTime > ?1 and n.status = 1")
    List<NotificationType> findByPushTime(Date date);

    NotificationType findByIdAndStatus(Long id,Integer status);


    List<NotificationType> findByTypeAndClassIdOrderByGmtCreateDesc(int type ,long classId);


}
