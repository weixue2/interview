package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
import com.huatu.tiku.interview.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 20:17
 * @Description
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;
    @Override
    public List<NotificationType> findAll() {
        return notificationTypeRepository.findAll();
    }
}
