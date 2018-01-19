package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.huatu.tiku.interview.constant.NotificationTypeConstant.REGISTER_REPORT;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 20:12
 * @Description
 */
@RestController
@RequestMapping("notify")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping
    public Result getAll(){
        List<NotificationType> all = notificationService.findAll();
        return all.isEmpty()?Result.build(ResultEnum.ERROR):Result.ok();
    }


    /**
     * 新增修改报道通知
     */
    @PostMapping("/registerReport")
    public Result saveRegisterReport(@RequestBody NotificationType registerReport){

        //校验通知类型
        if( REGISTER_REPORT.getCode()  != registerReport.getType()){
            return  Result.build(ResultEnum.NOTIFICATION_TYPE_ERROR);
        }
        //校验推送时间
        if(null == registerReport.getPushTime()){
            return  Result.build(ResultEnum.PUSH_TIME_ERROR);
        }
        return notificationService.saveRegisterReport(registerReport) == null ?Result.ok():Result.build(ResultEnum.INSERT_FAIL);
    }
}
