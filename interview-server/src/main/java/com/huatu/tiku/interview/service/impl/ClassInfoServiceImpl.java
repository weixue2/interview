package com.huatu.tiku.interview.service.impl;

import com.huatu.tiku.interview.entity.po.ClassInfo;
import com.huatu.tiku.interview.repository.ClassInfoRepository;
import com.huatu.tiku.interview.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/25 20:03
 * @Description
 */
@Service
public class ClassInfoServiceImpl implements ClassInfoService {

    @Autowired
    private ClassInfoRepository classInfoRepository;

    @Override
    public List<ClassInfo> getList() {
        return classInfoRepository.findAll();
    }
}
