package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.entity.po.ClassInfo;
import com.huatu.tiku.interview.service.ClassInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/25 19:59
 * @Description
 */
@RestController
@RequestMapping("/end/cc")
public class ClussController {

    @Autowired
    private ClassInfoService classInfoService;

    @GetMapping("list")
    public List<ClassInfo> getList(){

        return classInfoService.getList();
    }
}
