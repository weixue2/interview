package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.constant.TemplateEnum;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.entity.template.MyTreeMap;
import com.huatu.tiku.interview.entity.template.TemplateMap;
import com.huatu.tiku.interview.entity.template.TemplateMsgResult;
import com.huatu.tiku.interview.entity.template.WechatTemplateMsg;
import com.huatu.tiku.interview.service.NotificationService;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.service.WechatTemplateMsgService;
import com.huatu.tiku.interview.util.common.PageUtil;
import com.huatu.tiku.interview.util.json.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    WechatTemplateMsgService templateMsgService;

    @Autowired
    StringRedisTemplate redis;

    @Autowired
    UserService userService;

    @GetMapping("pushNotify")
    public Result pushNotify(){
        String accessToken = redis.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY);
        WechatTemplateMsg templateMsg ;
        for (User u : userService.findAllUser()) {
            templateMsg = new WechatTemplateMsg(u.getOpenId(), TemplateEnum.MorningReading);
            templateMsg.setUrl(BasicParameters.MorningReadingURL+6);
            templateMsg.setData(
                    MyTreeMap.createMap(
                            new TemplateMap("first", WechatTemplateMsg.item("今日热点已新鲜出炉~", "#000000")),
                            new TemplateMap("keyword1", WechatTemplateMsg.item(u.getName(), "#000000")),
                            new TemplateMap("keyword2", WechatTemplateMsg.item("我哪知道", "#000000")),
                            new TemplateMap("remark", WechatTemplateMsg.item("华图在线祝您顺利上岸！", "#000000"))
                    )
            );
            templateMsgService.sendTemplate(accessToken, JsonUtil.toJson(templateMsg));
        }
        return Result.ok();
    }

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

        NotificationType notificationType = notificationService.saveRegisterReport(registerReport);
        return  notificationType != null ?Result.ok(notificationType):Result.build(ResultEnum.INSERT_FAIL);
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


    /**
     * 查询报道通知详情
     */
    @GetMapping("/registerReport/{id}")
    public Result detail(@PathVariable  Long id){
        NotificationType notificationType = notificationService.findOne(id);
        return notificationType != null ? Result.ok(notificationType):Result.build(ResultEnum.INSERT_FAIL);
    }

}
