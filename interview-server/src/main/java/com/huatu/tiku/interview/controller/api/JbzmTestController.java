package com.huatu.tiku.interview.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huatu.tiku.interview.constant.BasicParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author jbzm
 * @Date Create on 2018/1/20 16:10
 */
@Controller
@Slf4j
public class JbzmTestController {
    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/lol", method = RequestMethod.GET)
    public String weixinRedirect(HttpServletRequest request, HttpServletResponse response) {
        log.info("--------------开始oauth跳转------------");
        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + BasicParameters.appID + "&redirect_uri=http://weixin.htexam.com/wx/api/lr/oauth?response_type=code&scope=snsapi_base&state=1&connect_redirect=1#wechat_redirect";
    }


    @RequestMapping(value = "/oauth", method = RequestMethod.GET)
    public ModelAndView weixinOAuth(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //得到code
        String CODE = request.getParameter("code");
        String APPID = BasicParameters.appID;
        String SECRET = BasicParameters.appsecret;
        //换取access_token 其中包含了openid
        String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code".replace("APPID", APPID).replace("SECRET", SECRET).replace("CODE", CODE);
        //URLConnectionHelper是一个模拟发送http请求的类
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse execute = httpClient.execute(httpGet);
        HttpEntity entity = execute.getEntity();
        String jsonStr = EntityUtils.toString(entity);
        JSONObject jsonObject = (JSONObject) JSON.parse(jsonStr);
        String openid = jsonObject.get("openid").toString();
        //有了用户的opendi就可以的到用户的信息了
        //得到用户信息之后返回到一个页面
        ModelAndView view = new ModelAndView();
        view.setViewName("redirect:http://tkproc.huatu.com/interview-h5/#/fill_information?page=2&openId=" + openid);
        return view;
    }
}
