package com.huatu.tiku.interview.service;

import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 17:04
 * @Description 在线课程安排
 */
public interface OnlineCourseArrangementService {
    Boolean add(OnlineCourseArrangement arrangement);
    // 这个其实是多余的，我跟你讲
    Boolean update(OnlineCourseArrangement arrangement);
    void del(Long id);

}
