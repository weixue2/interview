package com.huatu.tiku.interview.controller.admin;

import com.huatu.common.ErrorResult;
import com.huatu.common.exception.BizException;
import com.huatu.tiku.interview.entity.po.Admin;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author jbzm
 * @Date Create on 2018/1/23 10:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/end/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {
    @Autowired
    private AdminService adminService;

    @GetMapping()
    public Result  login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        log.info("username: {}  ,password:{} ", username, password);
        Admin admin = adminService.login(username, password);
        if (admin == null) {
            throw new BizException(ErrorResult.create(405, "用户名或密码错误"));
        } else {
            request.getSession().setAttribute("user", admin);
        }
        return Result.ok(admin);
    }
}
