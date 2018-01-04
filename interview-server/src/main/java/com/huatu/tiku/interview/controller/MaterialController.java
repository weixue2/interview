package com.huatu.tiku.interview.controller;

import com.huatu.tiku.interview.entity.material.MaterialList;
import com.huatu.tiku.interview.service.MaterialService;
import com.huatu.tiku.interview.task.AccessTokenThread;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
/**
 * @author zhouwei
 * @Description: 与微信对接登陆验证
 * @create 2018-01-04 上午11:53
 **/
@RestController
@Slf4j
@RequestMapping("/api/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;

    private static Logger log = LoggerFactory.getLogger(MaterialController.class);



    //上传临时素材
    @RequestMapping(value="/addtem",method = RequestMethod.POST)
    public String add(){
        // 调用接口获取access_token
        String at = AccessTokenThread.accessToken.getAccess_token();
        //这里只是单纯地用图片image来测试

        if(at!=null){
            String i=materialService.uploadPermanentMedia2(at,null,null);
            log.info("media_id:"+i);
            return i;
        }else {
            log.error("显示失败");
            return "获取media_id失败";
        }
    }

    //上传永久素材
    @RequestMapping(value="/addper",method = RequestMethod.POST)
    public String addper(){
        // 调用接口获取access_token
        String at = AccessTokenThread.accessToken.getAccess_token();
        //这里只是单纯地用图片image来测试
        if(at!=null){
            String i=materialService.uploadPermanentMedia1(at,null,null);
            log.info("media_id:"+i);
            return i;
        }else {
            log.error("显示失败");
            return "获取media_id失败";
        }
    }
    //根据media_id来获取素材，这里先不写，以后再完善
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public String get(){
        return null;
    }


    //获取素材总数
    @RequestMapping(value="/getall",method = RequestMethod.GET)
    public ModelAndView getall(){
        // 调用接口获取access_token
        String at = AccessTokenThread.accessToken.getAccess_token();
        String url="https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token="+at;
        ModelAndView mv=new ModelAndView("redirect:"+url);
        return mv;
    }

    //获取素材列表
    @RequestMapping(value = "/getlist",method = RequestMethod.POST)
    public String getlist(){
        // 调用接口获取access_token
        String at = AccessTokenThread.accessToken.getAccess_token();
        if(at!=null) {
            String a=materialService.getlist(getlist1(), at);
            log.info("获取素材列表为："+a);
            return a;
        }else {
            log.error("获取失败");
            return "error";
        }
    }

    //新建一个固定的素材列表信息
    public MaterialList getlist1() {
        MaterialList materialList = new MaterialList();
        materialList.setType("image");//返回图片素材
        materialList.setOffset("0");//从第一个素材返回
        materialList.setCount("20");//返回20个素材
        return materialList;
    }
}
