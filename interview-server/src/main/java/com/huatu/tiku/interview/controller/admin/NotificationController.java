package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.NotificationService;
import com.huatu.tiku.interview.util.common.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.huatu.tiku.interview.constant.NotificationTypeConstant.REGISTER_REPORT;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/18 20:12
 * @Description
 */
@RestController
@RequestMapping("/end/notify")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping
    public Result getPage(@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam(name = "page", defaultValue = "1") Integer page){
        PageUtil<List<NotificationType>> all = notificationService.findAll(pageSize,page);
        return all.getResult().isEmpty()?Result.build(ResultEnum.ERROR):Result.ok(all);
    }

    @GetMapping("fuzzy")
    public Result fuzzy(@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, @RequestParam(name = "page", defaultValue = "1") Integer page,String title){
        PageUtil<List<NotificationType>> all = notificationService.findByTitleLimit(pageSize,page,title);
        System.out.println();
        return all.getResult().isEmpty()?Result.ok():Result.ok(all);
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
        return notificationService.saveRegisterReport(registerReport) != null ?Result.ok():Result.build(ResultEnum.INSERT_FAIL);
    }

    /**
     * 删除学员学习情况
     * @param id
     * @return
     */
    @DeleteMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result del(@PathVariable  Long id){

        return notificationService.del(id) != 0 ?Result.ok():Result.build(ResultEnum.DELETE_FAIL);
    }


}
