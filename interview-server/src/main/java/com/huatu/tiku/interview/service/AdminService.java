package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.Admin;

/**
 * @Author jbzm
 * @Date Create on 2018/1/23 10:28
 */
public interface AdminService {
    Admin login(String username, String password);
}
