package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhouwei
 * @Description: TODO
 * @create 2018-01-05 下午4:29
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {
    User getUserByOpenIdAndStatus(String openId,int status);
    User findByOpenId(String openId);
}
