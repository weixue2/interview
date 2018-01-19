package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.util.common.PageUtil;

import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 20:17
 * @Description
 */
public interface NotificationService {

    NotificationType saveRegisterReport(NotificationType registerReport);
    PageUtil<List<NotificationType>> findAll(Integer size, Integer page);
    PageUtil<List<NotificationType>> findByTitleLimit( Integer size,Integer page,String title);
    NotificationType get(Long id);
}
