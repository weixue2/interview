package com.huatu.tiku.interview.userHandler.event.impl;

import com.huatu.common.utils.date.DateFormatUtil;
import com.huatu.common.utils.date.DateUtil;
import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.NotificationTypeConstant;
import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.po.SignIn;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.entity.po.UserClassRelation;
import com.huatu.tiku.interview.repository.*;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.userHandler.event.EventHandler;
import com.huatu.tiku.interview.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: ZhenYang
 * @Date: Created in 2018/1/12 10:07
 * @Modefied By:
 * 用户事件处理类
 */
@Component
@Slf4j
public class EventHandlerImpl implements EventHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private SignInRepository signInRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationTypeRepository notificationTypeRepository;
    @Autowired
    private LearningReportRepository learningReportRepository;
    @Value("${phone_check}")
    private String phoneCheck;
    @Autowired
    private UserClassRelationRepository userClassRelationRepository;
    private static final String SIGN_SUCC_MSG = "恭喜，您于%s签到成功。";


    @Override
    public String subscribeHandler(Map<String, String> requestMap) {
        // TODO 因为可以验证取关的事件，所以这里的逻辑可以删掉
        String fromUserName = requestMap.get("FromUserName");
        User user = userService.getUserByOpenId(fromUserName);
        if (user == null) {
            userService.createUser(fromUserName);
        }
        NewsMessage nm = new NewsMessage(requestMap);
        List<Article> as = new ArrayList<>();
        Article a = new Article();
        a.setTitle("谢谢您的关注！!");
        a.setDescription("点击图文可以跳转到华图首页");
        a.setPicUrl(BasicParameters.IMAGE_SUBSCRIBE_001);
        //这里跳转前端验证
        a.setUrl(phoneCheck + "openId=" + fromUserName);
        as.add(a);
        nm.setArticleCount(as.size());
        nm.setArticles(as);
        return MessageUtil.MessageToXml(nm);
    }


    @Override
    public String unsubscribeHandler(Map<String, String> requestMap) {
        return null;
    }

    /**
     * 这东西太他妈太反人类了
     *
     * @param requestMap
     * @return
     */
    @Override
    public String signInHandler(Map<String, String> requestMap) {
        String str;
        log.info("开始判断该二维码是否我为华图官方签到二维码:" + requestMap.get("EventKey"));
        if ("sign_in".equals(requestMap.get("EventKey"))) {
            String h = new SimpleDateFormat("HH").format(new Date());
            //设置签到时间    08:00-09:00    13:00-14:00   18:00-19:00
            User user = userRepository.findByOpenId(requestMap.get("FromUserName"));
            if ((user != null || user.getStatus() == 1)) {
                if (Integer.parseInt(h) < 9 && Integer.parseInt(h) >= 8 || Integer.parseInt(h) < 14 && Integer.parseInt(h) >= 13 || Integer.parseInt(h) < 19 && Integer.parseInt(h) >= 18) {
                    log.info("开始签到");
                    String time = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date());
                    String timeStr = String.format(SIGN_SUCC_MSG, time);
                    str = WxMpXmlOutMessage
                            .TEXT()
                            .content(timeStr)
                            .fromUser(requestMap.get("ToUserName"))
                            .toUser(requestMap.get("FromUserName"))
                            .build()
                            .toXml();
                    SignIn signIn = new SignIn();
                    signIn.setOpenId(requestMap.get("FromUserName"));
                    signIn.setSignTime(new Date());
                    signIn.setBizStatus(1);
                    signIn.setStatus(1);
                    signInRepository.save(signIn);
                } else {
                    log.info("签到失败");
                    str = WxMpXmlOutMessage
                            .TEXT()
                            .content("未在签到时间内签到")
                            .fromUser(requestMap.get("ToUserName"))
                            .toUser(requestMap.get("FromUserName"))
                            .build().toXml();
                }
            } else {
                log.info("该用户不是100w项目用户");
                str = WxMpXmlOutMessage
                        .TEXT()
                        .content("签到失败")
                        .fromUser(requestMap.get("ToUserName"))
                        .toUser(requestMap.get("FromUserName"))
                        .build().toXml();
            }
        } else {
            log.info("非签到二维码");
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("该二维码不可用于签到")
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build().toXml();
        }
        return str;
    }

    /**
     * 处理点击事件
     *
     * @param requestMap
     * @return
     */
    @Override
    public String eventClick(Map<String, String> requestMap) {
        String str = null;
        if ("course".equals(requestMap.get("EventKey"))) {
            User user = userRepository.findByOpenId(requestMap.get("FromUserName"));
            if ((user == null || user.getStatus() != 1)) {
                log.info("----查询不到用户信息----");
                str = WxMpXmlOutMessage
                        .TEXT()
                        .content("抱歉，经系统核实您的手机号未购买“2018国考封闭特训班”~若有疑问，请联系客服：400-817-6111")
                        .fromUser(requestMap.get("ToUserName"))
                        .toUser(requestMap.get("FromUserName"))
                        .build()
                        .toXml();
            } else {
                // 查询用户所属班级
                String classId = "0";
                //查询用户所属班级
                List<UserClassRelation> userClassRelationList = userClassRelationRepository.findByOpenIdAndStatus(user.getOpenId(), WXStatusEnum.Status.NORMAL.getStatus());
                if (CollectionUtils.isEmpty(userClassRelationList)) {
                    log.info("用户没有所属班级，为用户推送最新默认课表");
                } else {
                    UserClassRelation userClassRelation = userClassRelationList.get(0);
                    classId = userClassRelation.getClassId() + "";
                }
                // 查询用户所属班级的课表图片
                List<NotificationType> imageList = notificationTypeRepository.findByTypeAndClassIdsLikeOrderByGmtCreateDesc(NotificationTypeConstant.ONLINE_COURSE_ARRANGEMENT.getCode(), "%" + classId + "%");

//                List<NotificationType> notTypePatterns = notificationTypeRepository.findByBizStatusAndStatus
//                        (new Sort(Sort.Direction.DESC, "gmtModify"), WXStatusEnum.BizStatus.ONLINE.getBizSatus(), WXStatusEnum.Status.NORMAL.getStatus());
                if (CollectionUtils.isNotEmpty(imageList)) {
                    for (NotificationType notificationType : imageList) {
                        if (StringUtils.isNotEmpty(notificationType.getWxImageId())) {
                            log.info("----展示图片----");
                            log.info("----图片id:" + notificationType.getWxImageId());
                            str = WxMpXmlOutMessage
                                    .IMAGE()
                                    .mediaId(notificationType.getWxImageId())
                                    .fromUser(requestMap.get("ToUserName"))
                                    .toUser(requestMap.get("FromUserName"))
                                    .build()
                                    .toXml();
                            break;
                        }
                    }
                } else {
                    str = WxMpXmlOutMessage
                            .TEXT()
                            .content("暂无课程安排，请联系助教")
                            .fromUser(requestMap.get("ToUserName"))
                            .toUser(requestMap.get("FromUserName"))
                            .build()
                            .toXml();

                }
            }
        } else if ("conn_service".equals(requestMap.get("EventKey"))) {
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("客服电话：400-817-6111")
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build()
                    .toXml();
        } else {
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("正在开发")
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build()
                    .toXml();
        }
        log.info("----返回的xml:" + str);
        return str;
    }
}
