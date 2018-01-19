package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author jbzm
 * @Date Create on 2018/1/19 10:55
 */
@Slf4j
@RestController
@RequestMapping(value = "end/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserEndController {
    @Autowired
    private UserService userService;
    @GetMapping
    public Result findUser() {
        return Result.ok(userService.findAllUser());
    }

    @PostMapping
    public Result updateUser(@RequestBody User user, HttpServletRequest request){
        user.setStatus(1);
        return userService.updateUser(user,request)? Result.ok(): Result.build(ResultEnum.INSERT_FAIL);
    }
}
