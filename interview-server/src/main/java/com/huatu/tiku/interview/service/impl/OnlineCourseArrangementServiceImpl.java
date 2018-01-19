package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
import com.huatu.tiku.interview.service.OnlineCourseArrangementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author jbzm
 * @Date Create on 2018/1/17 17:21
 */
@Service
public class OnlineCourseArrangementServiceImpl implements OnlineCourseArrangementService {

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    @Override
    public Boolean add(NotificationType notificationType) {
        notificationType.setBizStatus(WXStatusEnum.BizStatus.ONLINE.getBizSatus());
        notificationType.setStatus(WXStatusEnum.Status.NORMAL.getStatus());
        notificationType.setType(1);
        notificationTypeRepository.save(notificationType);
        return true;
    }

    @Override
    public Object findById(Long id) {
        return notificationTypeRepository.findOne(id);
    }
}
