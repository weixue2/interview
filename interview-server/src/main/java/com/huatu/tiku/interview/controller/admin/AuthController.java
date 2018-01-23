package com.huatu.tiku.interview.controller.admin;

import com.huatu.common.BaseResult;
import com.huatu.common.LoginResult;
import com.huatu.common.Result;
import com.huatu.tiku.interview.constant.WebParamConsts;
import com.sun.xml.internal.rngom.parse.host.Base;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hanchao
 * @date 2017/12/27 9:56
 */
@RestController
@RequestMapping("/end/auth")
public class AuthController {
    /**
     * 适配session过期等的检查
     *
     * @return
     */
    @RequestMapping("/tologin")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result tologin() {
        return LoginResult.UNAUTHORIZED;
    }

    @RequestMapping("/denied")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result denied() {
        return LoginResult.FORBIDDEN;
    }

    /**
     * 获取账户信息的接口
     *
     * @return
     */
    @RequestMapping("/get")
    public Object get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return principal;
        } else {
            return LoginResult.UNAUTHORIZED;
        }
    }


    @RequestMapping(value = "/success", params = "login")
    public BaseResult loginSuccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return BaseResult.create(20000, "", principal);
    }

    @RequestMapping(value = "/success", params = "logout")
    public BaseResult logoutSuccess() {
        return BaseResult.create(1000000,"操作成功");
    }

    /**
     * 登陆失败返回信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/fail")
    public Result fail(HttpServletRequest request) {
        AuthenticationException authenticationException = (AuthenticationException) request.getAttribute(WebParamConsts.SPRING_SECURITY_EX);
//        if(authenticationException instanceof CaptchaInvalidException){
//            return MorphlingResponse.WRONG_CAPTCHA;
//        }else if(authenticationException instanceof UsernameNotFoundException){
//            return MorphlingResponse.USERNAME_NOT_EXIST;
//        }else if(authenticationException instanceof BadCredentialsException){
//            return MorphlingResponse.WRONG_PASSWORD;
//        }else{
//            String message = authenticationException.getMessage();
//            return ErrorResult.create(MorphlingResponse.LOGIN_FAILED.getCode(),message);
//        }
        return LoginResult.LOGIN_FAILED;
    }


//    @RequestMapping("/modify")
//    public Result modify(@UserSession UserInfo userInfo, String oldPass, String newPass){
//        User logined = userService.get(userInfo.getId());
//        if(passwordEncoder.matches(oldPass,logined.getPassword())){
//            logined.setPassword(passwordEncoder.encode(newPass));
//            userService.save(logined);
//            return SuccessMessage.create();
//        }
//        return MorphlingResponse.WRONG_PASSWORD;
//    }
}
