package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.ClassInfo;
import com.huatu.tiku.interview.entity.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 班级信息表
 **/
@Repository
public interface ClassInfoRepository extends JpaRepository<ClassInfo, Long>,JpaSpecificationExecutor<User> {

}
