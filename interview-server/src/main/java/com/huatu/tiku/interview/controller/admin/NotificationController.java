package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.NotificationService;
import com.huatu.tiku.interview.util.common.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public Result getPage(@RequestParam(name = "size", defaultValue = "10") Integer size, @RequestParam(name = "page", defaultValue = "1") Integer page){
        PageUtil<List<NotificationType>> all = notificationService.findAll(size,page);
        return all.getResult().isEmpty()?Result.build(ResultEnum.ERROR):Result.ok(all);
    }

    @GetMapping("fuzzy")
    public Result fuzzy(@RequestParam(name = "size", defaultValue = "10") Integer size, @RequestParam(name = "page", defaultValue = "1") Integer page,String title){
        PageUtil<List<NotificationType>> all = notificationService.findByTitleLimit(size,page,title);
        System.out.println();
        return all.getResult().isEmpty()?Result.ok():Result.ok(all);
    }
}
