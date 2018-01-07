package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.util.CheckUtil;
import com.huatu.tiku.interview.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zhouwei
 * @Description: 与微信对接登陆验证
 * @create 2018-01-04 上午11:53
 **/
@RestController
@Slf4j
@RequestMapping
public class LoginController {
    @GetMapping(value = "")
    public void checkSignature(@RequestParam(name = "signature" ,required = false) String signature  ,
                                 @RequestParam(name = "nonce",required = false) String  nonce ,
                                 @RequestParam(name = "timestamp",required = false) String  timestamp ,
                                 @RequestParam(name = "echostr",required = false) Object  echostr,HttpServletResponse resp){
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        log.info("-------开始验证----------signature:{},nonce:{},timestamp:{},echostr:{}",signature,nonce,timestamp,echostr);
        try {

            PrintWriter out = resp.getWriter();
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            log.info("接入成功");
          //  return echostr;

                out.print(echostr);
                return;

        }
            log.error("接入失败");
            out.print(echostr);
        } catch (IOException e) {
                e.printStackTrace();
            }

      //  return echostr;
    }
}
