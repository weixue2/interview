package com.huatu.tiku.interview.service.impl;

import com.google.common.collect.Lists;
import com.huatu.tiku.interview.entity.dto.NotificationVO;
import com.google.common.collect.Lists;
import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.dto.NotificationVO;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
import com.huatu.tiku.interview.service.NotificationService;
import com.huatu.tiku.interview.util.GetAllParameter;
import com.huatu.tiku.interview.util.common.PageUtil;
import com.huatu.tiku.interview.util.GetAllParameter;
import com.huatu.tiku.interview.util.common.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        List<NotificationVO> notificationVOs = GetAllParameter.test(all.getContent(), NotificationVO.class);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long totalElements = all.getTotalElements();
        PageUtil resultPageUtil = PageUtil.builder().result(notificationVOs)
                .total(totalElements)
                .totalPage(0 == totalElements % pageSize ? totalElements / pageSize : totalElements / pageSize + 1)
                .next(totalElements > pageSize * pageNumber ? 1 : 0)
                .build();
        return resultPageUtil;

    }

    @Override
    public PageUtil<List<NotificationType>> findByTitleLimit(Integer size, Integer page, String title) {
        PageRequest pageable = new PageRequest(page-1,size,new Sort("gmtCreate"));
        Specification<NotificationType> specification = selectRules(title);
        Page<NotificationType> all = notificationTypeRepository.findAll(specification, pageable);
        List<NotificationVO> notificationVOs = GetAllParameter.test(all.getContent(), NotificationVO.class);
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long totalElements = all.getTotalElements();
        PageUtil resultPageUtil = PageUtil.builder().result(notificationVOs)
                .total(totalElements)
                .totalPage(0 == totalElements % pageSize ? totalElements / pageSize : totalElements / pageSize + 1)
                .next(totalElements > pageSize * pageNumber ? 1 : 0)
                .build();
        return resultPageUtil;
    }
    private <T> Specification<T> selectRules(String title) {
        Specification specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = Lists.newArrayList();
                if (title != null) {
                    predicates.add(cb.like(root.get("title"), "%"+title+"%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }

    @Override
    public NotificationType saveRegisterReport(NotificationType registerReport) {

        registerReport.setStatus(WXStatusEnum.Status.NORMAL.getStatus());
        registerReport.setBizStatus(WXStatusEnum.BizStatus.ONLINE.getBizSatus());
        return notificationTypeRepository.save(registerReport);
    }
}
