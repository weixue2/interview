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

    /**
     * 删除的时候记得要连带图片文件
     * @param id
     */
    Boolean del(Long id);

}
