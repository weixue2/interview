package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;
import com.huatu.tiku.interview.entity.result.ReqResult;
import com.huatu.tiku.interview.service.OnlineCourseArrangementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 18:59
 * @Description
 */
@RestController
@RequestMapping("/api/oca")
public class OnlineCourseArrangementController {

    @Autowired
    private OnlineCourseArrangementService arrangementService;

    @PostMapping("insertOnlineCourseArrangement") //@requestBody --> Json
    public ReqResult add(OnlineCourseArrangement onlineCourseArrangement,@RequestParam("file") MultipartFile file){
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        System.out.println("contentType:"+contentType+"---fileName:"+fileName);
        return arrangementService.add(onlineCourseArrangement)? ReqResult.ok():ReqResult.build(ResultEnum.insertFail);
    }
}
