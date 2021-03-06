package com.huatu.tiku.interview.service.impl;

import com.google.common.collect.Lists;
import com.huatu.tiku.interview.constant.WXStatusEnum;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.Date;
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
        PageRequest pageable = new PageRequest(page-1,size,new Sort(Sort.Direction.DESC,"gmtCreate"));
        Specification<NotificationType> specification = selectRules();
        Page<NotificationType> all = notificationTypeRepository.findAll(specification,pageable);
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
        PageRequest pageable = new PageRequest(page-1,size,new Sort(Sort.Direction.DESC,"gmtCreate"));
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

    @Override
    public NotificationType get(Long id) {
        Specification<NotificationType> specification = selectRules(id);
        return notificationTypeRepository.findOne(specification);
    }

    @Override
    public List<NotificationType> findByPushTime() {
        return notificationTypeRepository.findByPushTime(new Date());
    }


    private <T> Specification<T> selectRules(Long id) {
        Specification specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = Lists.newArrayList();
                if (id != null) {
                    predicates.add(cb.equal(root.get("id"), id));
                    predicates.add(cb.equal(root.get("status"),WXStatusEnum.Status.NORMAL.getStatus()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }
    private <T> Specification<T> selectRules() {
        Specification specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = Lists.newArrayList();
                    predicates.add(cb.equal(root.get("status"),WXStatusEnum.Status.NORMAL.getStatus()));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }

    private <T> Specification<T> selectRules(String title) {
        Specification specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = Lists.newArrayList();
                if (title != null) {
                    predicates.add(cb.like(root.get("title"), "%"+title+"%"));
                    predicates.add(cb.equal(root.get("status"),WXStatusEnum.Status.NORMAL.getStatus()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }

    @Override
    public NotificationType saveRegisterReport(NotificationType registerReport) {
        registerReport.setCreator("admin");
        registerReport.setStatus(WXStatusEnum.Status.NORMAL.getStatus());
        registerReport.setBizStatus(WXStatusEnum.BizStatus.ONLINE.getBizSatus());
        return notificationTypeRepository.save(registerReport);
    }



    @Override
    public int del(Long id) {
       return  notificationTypeRepository.updateToDel(id);
    }

    @Override
    public NotificationType findOne(Long id) {
        return notificationTypeRepository.findByIdAndStatus(id,WXStatusEnum.Status.NORMAL.getStatus());
    }
}
