package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.service.CoreService;
import com.huatu.tiku.interview.task.AccessTokenThread;
import com.huatu.tiku.interview.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhouwei
 * @Description:
 * @create 2018-01-04 上午11:53
 **/
@RestController
@Slf4j
public class CoreController {
    @Autowired
    private CoreService coreService;


    @PostMapping(value = "", produces = "application/xml; charset=UTF-8")
    public String post(HttpServletRequest req) {
        // 调用核心业务类接收消息、处理消息跟推送消息
        log.info("--------------core-------------------");
        String respMessage = coreService.processRequest(req);
        return respMessage;
    }

    @GetMapping("token")
    public String getToken() {
        return AccessTokenThread.accessToken.getAccess_token();
    }

}
