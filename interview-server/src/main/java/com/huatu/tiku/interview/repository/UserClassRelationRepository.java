package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.UserClassRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学员-班级关系表
 **/
@Repository
public interface UserClassRelationRepository extends JpaRepository<UserClassRelation, Long>,JpaSpecificationExecutor<UserClassRelation> {

    List<UserClassRelation> findByOpenIdAndStatus(String openId,int status);

}
