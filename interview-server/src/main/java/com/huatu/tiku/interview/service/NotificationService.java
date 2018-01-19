package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.NotificationType;

import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 20:17
 * @Description
 */
public interface NotificationService {
    List<NotificationType> findAll();

    NotificationType saveRegisterReport(NotificationType registerReport);
}
