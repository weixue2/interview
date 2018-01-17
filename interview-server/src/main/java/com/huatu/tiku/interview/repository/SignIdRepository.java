package com.huatu.tiku.interview.repository;

import com.huatu.tiku.interview.entity.po.SignIn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author jbzm
 * @Date Create on 2018/1/17 17:21
 */
public interface SignIdRepository extends JpaRepository<SignIn, Long> {

}
