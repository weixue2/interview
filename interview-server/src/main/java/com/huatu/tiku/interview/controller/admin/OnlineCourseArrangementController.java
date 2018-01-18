package com.huatu.tiku.interview.controller.admin;

import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;
import com.huatu.tiku.interview.entity.result.Result;
import com.huatu.tiku.interview.service.OnlineCourseArrangementService;
import com.huatu.tiku.interview.util.file.FileUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/13 18:59
 * @Description
 */
@RestController
@RequestMapping("/end/oca")
public class OnlineCourseArrangementController {

    @Autowired
    private OnlineCourseArrangementService arrangementService;

    @Autowired
    private FileUtil fileUtil;


    @PostMapping("CourseArrangement") //@requestBody --> Json 不行，这个因为有个文件，就用
    public Result add(OnlineCourseArrangement onlineCourseArrangement, @RequestParam("file") MultipartFile file, @RequestParam("title") String title) throws Exception {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        //TODO 有时间再改
        // 设置微信公众号的appid
        config.setAppId("wx53505056175d5968");
        // 设置微信公众号的app corpSecret
        config.setSecret("739040d83f6d5c73fa961e3b1a48540f");

        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);
        String fileUrl = fileUtil.ftpUploadArrangement(file);
        //转换为文件---坑死了
        File tempFile = FileUtils.createTmpFile(file.getInputStream(), UUID.randomUUID().toString(), file.getContentType().split("/")[1]);
        WxMpMaterial wxMpMaterial = new WxMpMaterial();
        wxMpMaterial.setFile(tempFile);
        wxMpMaterial.setName(WxConsts.MediaFileType.IMAGE);
        //通过微信服务器获取图片官方id
        WxMpMaterialUploadResult res = wxMpService.getMaterialService().materialFileUpload(WxConsts.MediaFileType.IMAGE, wxMpMaterial);
        //装入对象
        onlineCourseArrangement.setWxImageId(res.getMediaId());
        onlineCourseArrangement.setImageUrl(fileUrl);
        onlineCourseArrangement.setTitle(title);
        return arrangementService.add(onlineCourseArrangement) ? Result.ok(fileUrl) : Result.build(ResultEnum.INSERT_FAIL);
    }

    @DeleteMapping
    public Result del(Long id) {
        System.out.println("id:" + id);
        return arrangementService.del(id) ? Result.ok() : Result.build(ResultEnum.DELETE_FAIL);
    }
}
