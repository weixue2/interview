package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.constant.WeChatUrlConstant;
import com.huatu.tiku.interview.entity.po.NotificationType;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.OnlineCourseArrangementService;
import com.huatu.tiku.interview.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * @Author jbzm
 * @Date Create on 2018/1/17 17:21
 */
@RestController
@Slf4j
@RequestMapping(value = "/end/oca", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OnlineCourseArrangementController {

    @Autowired
    private OnlineCourseArrangementService arrangementService;

    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/CourseArrangement")
    public Result add(@RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam Long id) throws Exception {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        NotificationType notificationType = new NotificationType();
        //TODO 有时间再改
        // 设置微信公众号的appid
        config.setAppId(BasicParameters.appID);
        // 设置微信公众号的app corpSecret
        config.setSecret(BasicParameters.appsecret);
        config.setAccessToken((String) redisTemplate.opsForValue().get(WeChatUrlConstant.ACCESS_TOKEN_KEY));
        log.info("获取accesstoken:" + config.getAccessToken());
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);
        String fileUrl = fileUtil.ftpUploadArrangement(file);
        log.info("文件地址:" + fileUrl);
        //转换为文件---坑死了
        File tempFile = FileUtils.createTmpFile(file.getInputStream(), UUID.randomUUID().toString(), file.getContentType().split("/")[1]);
        WxMpMaterial wxMpMaterial = new WxMpMaterial();
        wxMpMaterial.setFile(tempFile);
        wxMpMaterial.setName(WxConsts.MediaFileType.IMAGE);
        //通过微信服务器获取图片官方id
        WxMpMaterialUploadResult res = wxMpService.getMaterialService().materialFileUpload(WxConsts.MediaFileType.IMAGE, wxMpMaterial);
        log.info("获取官方imageId:" + res.getMediaId());
        //装入对象
        notificationType.setWxImageId(res.getMediaId());
        notificationType.setImageUrl(fileUrl);
        notificationType.setTitle(title);
        notificationType.setId(id);
        arrangementService.add(notificationType);
        return Result.ok(notificationType);
    }

    @GetMapping("/CourseArrangement")
    public Result findById(@RequestParam Long id) {
        return Result.ok(arrangementService.findById(id));
    }
}
