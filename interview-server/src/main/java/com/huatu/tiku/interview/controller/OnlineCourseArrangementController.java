package com.huatu.tiku.interview.controller;

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
@RequestMapping("/api/oca")
public class OnlineCourseArrangementController {

    @Autowired
    private OnlineCourseArrangementService arrangementService;

    @Autowired
    private FileUtil fileUtil;


    @PostMapping("CourseArrangement") //@requestBody --> Json 不行，这个因为有个文件，就用
    public Result add(OnlineCourseArrangement onlineCourseArrangement, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId("wx53505056175d5968"); // 设置微信公众号的appid
        config.setSecret("739040d83f6d5c73fa961e3b1a48540f"); // 设置微信公众号的app corpSecret

        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);
        String fileUrl = fileUtil.ftpUploadArrangement(file);

        File tempFile = FileUtils.createTmpFile(file.getInputStream(),
                UUID.randomUUID().toString(), file.getContentType().split("/")[1]);
        WxMpMaterial wxMpMaterial = new WxMpMaterial();
        wxMpMaterial.setFile(tempFile);
        wxMpMaterial.setName(WxConsts.MediaFileType.IMAGE);
        WxMpMaterialUploadResult res = wxMpService.getMaterialService().materialFileUpload(WxConsts.MediaFileType.IMAGE, wxMpMaterial);
        onlineCourseArrangement.setWxImageId(res.getMediaId());
        onlineCourseArrangement.setImageUrl(fileUrl);
        return arrangementService.add(onlineCourseArrangement) ? Result.ok(fileUrl) : Result.build(ResultEnum.INSERT_FAIL);
    }

    @DeleteMapping
    public Result del(Long id) {
        System.out.println("id:" + id);
        return arrangementService.del(id) ? Result.ok() : Result.build(ResultEnum.DELETE_FAIL);
    }
//    @PostMapping("insertOnlineCourseArrangement") //@requestBody --> Json 不行，这个因为有个文件，就用
//    public Result add(OnlineCourseArrangement onlineCourseArrangement, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
//        Pattern pattern = Pattern.compile(BasicParameters.RegExForImageUpload);
//        // 忽略大小写
//        // Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(file.getOriginalFilename());
//        String[] strs = file.getContentType().split("/");
//        if (strs.length == 2) {
//            if (strs[0].equals("image")) {
//                if (matcher.find()) {
//                    String filePath = BasicParameters.fileUploadPath;
//                    String fileName = UUID.randomUUID().toString() + "." + strs[1];
//                    try {
//                        FileUtil.uploadFile(file.getBytes(), filePath, fileName);
//                    } catch (Exception e) {
//                        return Result.build(ResultEnum.fileError);
//                    } finally {
//                        // TODO 把这个文件存到？？？文件？那这里不就不需要写入了吗直接传啊
//                        onlineCourseArrangement.setImageUrl(filePath + fileName);
//                    }
//                    return arrangementService.add(onlineCourseArrangement) ? Result.ok() : Result.build(ResultEnum.insertFail);
//                }
//            }
//        }
//        return Result.build(ResultEnum.insertFail);
//    }


//    @PostMapping("insertOnlineCourseArrangement") //@requestBody --> Json 不行，这个因为有个文件，就用
//    public Result add(OnlineCourseArrangement onlineCourseArrangement, @RequestParam("file") MultipartFile file, HttpServletRequest request){
//        System.out.println(onlineCourseArrangement.getTitle());
//        String contentType = file.getContentType();
//        String MultipartFileName = file.getOriginalFilename();
//        System.out.println("contentType:"+contentType+"---fileName:"+MultipartFileName);
//
//
//        String regEx = ".+(.JPEG|.jpeg|.JPG|.jpg|.bmp|.BMP)$";
//        Pattern pattern = Pattern.compile(regEx);
//        // 忽略大小写的写法
//        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(MultipartFileName);
//        boolean rs = matcher.find();
//
//        String[] strs=contentType.split("/");
//        if(strs.length != 2){
//            return Result.build(ResultEnum.imageFormatError);
//        }
//        if(!strs[0].equals("image")){
//            return Result.build(ResultEnum.imageFormatError);
//        }else if(!rs){
//            return Result.build(ResultEnum.imageFormatError);
//        }
//
////        String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
//        String filePath = "imgupload/";
//        String uuid = UUID.randomUUID().toString();
////        String[] fileName_ = MultipartFileName.split(".");
////        if(fileName_.length != 2){
////            return Result.build(ResultEnum.imageFormatError);
////        }
//        String fileName = uuid+"."+strs[1];
//        try {
//            FileUtil.uploadFile(file.getBytes(),filePath , fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // TODO: handle exception
//        }finally {
//            onlineCourseArrangement.setImageUrl(filePath+fileName);
//        }
//
//        return arrangementService.add(onlineCourseArrangement)? Result.ok():Result.build(ResultEnum.insertFail);
//    }

}
