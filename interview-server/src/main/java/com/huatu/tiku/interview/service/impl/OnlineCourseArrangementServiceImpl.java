package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;
import com.huatu.tiku.interview.repository.OnlineCourseArrangementRepository;
import com.huatu.tiku.interview.service.OnlineCourseArrangementService;
import com.huatu.tiku.interview.util.file.FileUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author jbzm
 * @Date Create on 2018/1/17 17:21
 */
@Service
public class OnlineCourseArrangementServiceImpl implements OnlineCourseArrangementService {

    @Autowired
    private OnlineCourseArrangementRepository onlineCourseArrangementRepository;

    @Override
    public Boolean add(OnlineCourseArrangement data) {
        data.setBizStatus(WXStatusEnum.BizStatus.NORMAL.getBizSatus());
        data.setStatus(WXStatusEnum.Status.ONLINE.getStatus());
        return onlineCourseArrangementRepository.save(data) == null ? false : true;
    }
}
