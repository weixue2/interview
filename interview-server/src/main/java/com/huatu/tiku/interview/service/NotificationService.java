package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.util.common.PageUtil;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 20:17
 * @Description
 */
public interface NotificationService {
    PageUtil<List<NotificationType>> findAll(Integer size, Integer page);
}
