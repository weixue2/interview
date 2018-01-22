package com.huatu.tiku.interview.userHandler.event.impl;

import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.WXStatusEnum;
import com.huatu.tiku.interview.entity.Article;
import com.huatu.tiku.interview.entity.message.NewsMessage;
import com.huatu.tiku.interview.entity.po.LearningReport;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.po.SignIn;
import com.huatu.tiku.interview.entity.po.User;
import com.huatu.tiku.interview.repository.LearningReportRepository;
import com.huatu.tiku.interview.repository.NotificationTypeRepository;
import com.huatu.tiku.interview.repository.SignInRepository;
import com.huatu.tiku.interview.repository.UserRepository;
import com.huatu.tiku.interview.service.UserService;
import com.huatu.tiku.interview.userHandler.event.EventHandler;
import com.huatu.tiku.interview.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
    UserRepository userRepository;
    @Autowired
    private NotificationTypeRepository notificationTypeRepository;
    @Autowired
    private LearningReportRepository learningReportRepository;

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
        a.setUrl(BasicParameters.LINK_SUBSCRIBE_001+fromUserName);
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
        String h = new SimpleDateFormat("HH").format(new Date());
        String str;
        //设置签到时间
        //if (Integer.parseInt(h) < 9 && Integer.parseInt(h) > 8) {
        if (System.currentTimeMillis() % 2 == 1) {
            log.info("开始签到");
            str = WxMpXmlOutMessage
                    .TEXT()
                    .content("签到成功")
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
                    .content("签到失败")
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
            List<NotificationType> notTypePatterns = notificationTypeRepository.findByBizStatusAndStatus
                    (new Sort(Sort.Direction.DESC, "gmtModify"), WXStatusEnum.BizStatus.ONLINE.getBizSatus(), WXStatusEnum.Status.NORMAL.getStatus());
            str = WxMpXmlOutMessage
                    .IMAGE()
                    .mediaId(notTypePatterns.get(0).getWxImageId())
                    .fromUser(requestMap.get("ToUserName"))
                    .toUser(requestMap.get("FromUserName"))
                    .build()
                    .toXml();
        }else if("dailyReport".equals(requestMap.get("EventKey"))){
            //推送学员学习报告
            //校验用户状态 抱歉，您尚未填写个人信息，无法核实您的学员身份~
            String openId = requestMap.get("FromUserName");

            User user = userRepository.findByOpenIdAndStatus(openId, WXStatusEnum.Status.NORMAL.getStatus());


            if(user == null ){
                log.info("校验用户状态 抱歉，您尚未填写个人信息，无法核实您的学员身份~");
                str = WxMpXmlOutMessage
                        .TEXT()
                        .content("校验用户状态 抱歉，您尚未填写个人信息，无法核实您的学员身份~")
                        .fromUser(requestMap.get("ToUserName"))
                        .toUser(requestMap.get("FromUserName"))
                        .build().toXml();
                return str;
            }else{
                //判断报告是否已经生成
                List<LearningReport> learningReports = learningReportRepository.findByOpenIdOrderByIdAsc(openId);
                if(CollectionUtils.isEmpty(learningReports)){
                    log.info("学习报告尚未生成~");
                    str = WxMpXmlOutMessage
                            .TEXT()
                            .content("学习报告尚未生成~")
                            .fromUser(requestMap.get("ToUserName"))
                            .toUser(requestMap.get("FromUserName"))
                            .build().toXml();
                    return str;
                }

                NewsMessage nm = new NewsMessage(requestMap);
                List<Article> as = new ArrayList<>();
                Article a = new Article();
                a.setTitle("今日学习报告已更新。");
                a.setDescription("请点击“详情”查看报告完整信息");
                a.setPicUrl(BasicParameters.IMAGE_SUBSCRIBE_001);
                //用户openID写死在链接中
                a.setUrl(BasicParameters.DailyReportURL+requestMap.get("FromUserName"));
                as.add(a);
                nm.setArticleCount(as.size());
                nm.setArticles(as);
                return MessageUtil.MessageToXml(nm);
            }

        }else if ("user_info".equals(requestMap.get("user_info"))) {

            WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
            item.setDescription("description");
            item.setPicUrl("https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwjDpPCoyerYAhWxSd8KHSHUAOwQjRwIBw&url=%2Furl%3Fsa%3Di%26rct%3Dj%26q%3D%26esrc%3Ds%26source%3Dimages%26cd%3D%26cad%3Drja%26uact%3D8%26ved%3D%26url%3Dhttp%253A%252F%252Fwww.5011.net%252Fzt%252Flubenwei%252F%26psig%3DAOvVaw3Z6ykd4X2VISOGyhpaOpvV%26ust%3D1516676193601065&psig=AOvVaw3Z6ykd4X2VISOGyhpaOpvV&ust=1516676193601065");
            item.setTitle("点击修改个人信息");
            item.setUrl("www.baidu.com");

            str = WxMpXmlOutMessage.NEWS()
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
        return str;
    }
}
