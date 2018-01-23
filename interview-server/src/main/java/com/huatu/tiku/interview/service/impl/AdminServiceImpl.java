package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.po.Admin;
import com.huatu.tiku.interview.repository.AdminRepository;
import com.huatu.tiku.interview.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author jbzm
 * @Date Create on 2018/1/23 10:28
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin login(String username, String password) {
        log.info("----验证用户登录----");
        return adminRepository.findByUsernameAndPassword(username, password);
    }
}
