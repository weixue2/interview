package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.dto.NotificationVO;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
import com.huatu.tiku.interview.service.NotificationService;
import com.huatu.tiku.interview.util.GetAllParameter;
import com.huatu.tiku.interview.util.common.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public PageUtil<List<NotificationType>> findAll(Integer size,Integer page) {
        PageRequest pageable = new PageRequest(page-1,size,new Sort("gmtCreate"));
        Page<NotificationType> all = notificationTypeRepository.findAll(pageable);
        List<NotificationVO> EssayUserVOs = GetAllParameter.test(all.getContent(), NotificationVO.class);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long totalElements = all.getTotalElements();
        PageUtil resultPageUtil = PageUtil.builder().result(EssayUserVOs)
                .total(totalElements)
                .totalPage(0 == totalElements % pageSize ? totalElements / pageSize : totalElements / pageSize + 1)
                .next(totalElements > pageSize * pageNumber ? 1 : 0)
                .build();
        return resultPageUtil;

    }
}
