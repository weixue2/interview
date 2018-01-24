package com.huatu.tiku.interview.controller.admin;

import com.huatu.common.ErrorResult;
import com.huatu.common.exception.BizException;
import com.huatu.tiku.interview.entity.po.Admin;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.AdminService;
import com.huatu.tiku.interview.util.LogPrint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author jbzm
 * @Date Create on 2018/1/23 10:16
 */
@Slf4j
@RestController
@RequestMapping(value = "/end/auth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LoginController {
    @Autowired
    private AdminService adminService;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param request
     * @return
     */
    @LogPrint
    @PostMapping("login")
    public Result login(String username, String password, HttpServletRequest request) {
        log.info("username: {}  ,password:{} ", username, password);
        Admin admin = adminService.login(username, password);
        if (admin == null) {
            throw new BizException(ErrorResult.create(405, "用户名或密码错误"));
        } else {
            request.getSession().setAttribute("user", admin);
        }
        return Result.ok(admin);
    }

    /**
     * 退出登录
     *
     * @param request
     */
    @LogPrint
    @GetMapping("logout")
    public Result logout(HttpServletRequest request) {
        log.info("logout");
        request.getSession().setAttribute("user", null);
        request.getSession().removeAttribute("user");
        request.getSession().invalidate();
        return Result.ok();
    }
}
