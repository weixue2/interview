package com.huatu.tiku.interview.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonObject;
import com.huatu.common.SuccessMessage;
import com.huatu.common.exception.BizException;
import com.huatu.common.utils.env.IpUtils;
import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.result.PhpResult;
import com.huatu.tiku.interview.exception.ReqException;
import com.huatu.tiku.interview.service.MobileService;
import com.huatu.tiku.interview.util.Crypt3Des;
import com.huatu.tiku.interview.util.common.RegexConfig;
import com.huatu.tiku.interview.util.common.UserRedisKeys;
import com.huatu.ztk.sms.MDSmsUtil;
import com.huatu.ztk.sms.SmsUtil;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/10 21:55
 * @Modefied By:
 */
@Service
@Slf4j
public class MobilServiceImpl implements MobileService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public User checkPHP(String mobile,String openId,HttpServletRequest request) {
        // TODO 这里固定使用了一个openID
        openId = "od2aM0j6XSIwjAt2fExHeegjOWn8";
        String token = Crypt3Des.encryptMode("phone="+mobile+"&timeStamp="+System.currentTimeMillis());
        System.out.println("token:"+token);
        // 请求php
        String result = restTemplate.getForObject(WeChatUrlConstant.PHP_GET_USER_INFO+token,String.class,token);
        System.out.println("result:"+result);
        JSONObject jsonobject = JSONObject.fromObject(result);
        PhpResult phpResult= (PhpResult)JSONObject.toBean(jsonobject,PhpResult.class);
        // TODO 验证啊。。。
        if(!phpResult.getCode().equals("1")){
            throw new ReqException(ResultEnum.PHP_MOBILE_ERROR);
        }
        String userInfo = Crypt3Des.decryptMode(phpResult.getData());
        JSONObject jsonObject2 = JSONObject.fromObject(userInfo);
        String sex = jsonObject2.get("sex").toString();
        String phone = jsonObject2.get("phone").toString();
        User user = new User();
        user.setSex(Integer.valueOf(sex));
        user.setPhone(phone);
        user.setOpenId(openId);
        System.out.println(user);

        String clientIp = null;
        try {
            final String real = request.getHeader("X-Real-IP");
            final String forwardedFor = request.getHeader("X-Forwarded-For");
            String agent = request.getHeader("User-Agent");
            if (StringUtils.isBlank(agent)) {
                agent = request.getHeader("user-agent");
            }
            final UserAgent userAgent = UserAgent.parseUserAgentString(agent);
            if (userAgent.getOperatingSystem().getDeviceType() == DeviceType.COMPUTER
                    || !isLegalAgent(agent)) {
                log.info("filter mobile={},agent={},forwardedFor={}", mobile, agent, forwardedFor);

//                return SuccessMessage.create("发送验证码成功");
//                return user;
            }

            clientIp = Optional.ofNullable(IpUtils.getIpFromRequest(request)).orElse("unknow");
            log.info("client={} send sms,mobile={}", clientIp, mobile);
            log.info("forwardedFor={},mobile={}, agent={},realip={}", forwardedFor, mobile, agent, real);
        } catch (Exception e) {
            log.error("ex", e);
        }

        sendCaptcha(mobile, clientIp, true);
        request.getSession().setAttribute("openId",openId);
//        return SuccessMessage.create("发送验证码成功");
        return user;
    }

    @Override
    public void userCaptcha(String mobile, String captcha) {
        //验证码对应的key
        String captchaKey = String.format(UserRedisKeys.CAPTCHA_MOBILE, mobile);
        //实际验证码
        final Object actualCaptcha = redisTemplate.opsForValue().get(captchaKey);

        if (actualCaptcha == null) {
            throw new ReqException(ResultEnum.CAPTCHA_EXPIRE);
        }

        //验证码错误
        if (!captcha.equals(actualCaptcha.toString())) {
            throw new ReqException(ResultEnum.CAPTCHA_ERROR);
        }
    }

    /**
     * 简单判断agent是否合法
     *
     * @param agent
     * @return
     */
    private boolean isLegalAgent(String agent) {
        //安卓,ios,pc理论上都带agent,不带agent视为非法请求
        if (StringUtils.isBlank(agent)) {
            return false;
        }
        return agent.contains("okhttp") || agent.contains("netschool");
    }
    /**
     * 发送短信
     *
     * @param mobile   手机号
     * @param clientIp
     * @throws BizException
     */
    public void sendCaptcha(String mobile, String clientIp, boolean isZtk) throws BizException {
        mobile = StringUtils.trimToEmpty(mobile);

        final SetOperations operations = redisTemplate.opsForSet();

        /**
         * 验证请求合法性
         */
        if (operations.isMember(UserRedisKeys.REJECT_MOBILES, mobile)) {
            log.info("it is robot,reject mobile={}", mobile);
            return;
        }

        if (StringUtils.isNoneBlank(clientIp) && operations.isMember(UserRedisKeys.REJECT_IPS, clientIp)) {
            log.info("it is robot,reject clientIp={}", clientIp);
            return;
        }


        final boolean isMobile = Pattern.matches(RegexConfig.MOBILE_PHONE_REGEX, mobile);
        if (!isMobile) {//非法的手机号
            //暂时先这样
            throw new ReqException(ResultEnum.ERROR);
        }

        String captchaKey = String.format(UserRedisKeys.CAPTCHA_MOBILE, mobile);
        String captchaMarkKey = String.format(UserRedisKeys.USER_CAPTCHA_MARK, mobile);

        final ValueOperations valueOperations = redisTemplate.opsForValue();
        final Object markObj = redisTemplate.opsForValue().get(captchaMarkKey);

        //markObj存在，说明距离上次发送验证码的时间未超过1分钟
        if (markObj != null) {
            throw new ReqException(ResultEnum.ERROR);
        }

        //随机生成验证码
        String captcha = RandomStringUtils.randomNumeric(6);
        while (true) {
            if (!captcha.startsWith("0")) {//验证码不能以0开头
                break;
            }
            captcha = RandomStringUtils.randomNumeric(6);
        }

        //将验证码设置到redis里面,有效期设置为3分钟
        valueOperations.set(captchaKey, captcha, 3, TimeUnit.MINUTES);
        valueOperations.set(captchaMarkKey, mobile, 1, TimeUnit.MINUTES);
        //发送验证码

        if (isZtk) {
            SmsUtil.sendCaptcha(mobile, captcha);
        } else {
            MDSmsUtil.sendCaptcha(mobile, captcha);
        }
    }
}
