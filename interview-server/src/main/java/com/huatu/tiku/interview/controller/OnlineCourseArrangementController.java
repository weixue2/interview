package com.huatu.tiku.interview.controller;

import com.huatu.common.exception.BizException;
import com.huatu.tiku.interview.constant.BasicParameters;
import com.huatu.tiku.interview.constant.ResultEnum;
import com.huatu.tiku.interview.entity.po.OnlineCourseArrangement;
import com.huatu.tiku.interview.entity.result.ReqResult;
import com.huatu.tiku.interview.service.OnlineCourseArrangementService;
import com.huatu.tiku.interview.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @PostMapping("insertOnlineCourseArrangement") //@requestBody --> Json 不行，这个因为有个文件，就用
    public ReqResult add(OnlineCourseArrangement onlineCourseArrangement, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Pattern pattern = Pattern.compile(BasicParameters.RegExForImageUpload);
        // 忽略大小写
        // Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(file.getOriginalFilename());
        String[] strs = file.getContentType().split("/");
        if (strs.length == 2) {
            if (strs[0].equals("image")) {
                if (matcher.find()) {
                    String filePath = BasicParameters.fileUploadPath;
                    String fileName = UUID.randomUUID().toString() + "." + strs[1];
                    try {
                        FileUtil.uploadFile(file.getBytes(), filePath, fileName);
                    } catch (Exception e) {
                        return ReqResult.build(ResultEnum.fileError);
                    } finally {
                        onlineCourseArrangement.setImageUrl(filePath + fileName);
                    }
                    return arrangementService.add(onlineCourseArrangement) ? ReqResult.ok() : ReqResult.build(ResultEnum.insertFail);
                }
            }
        }
        return ReqResult.build(ResultEnum.insertFail);
    }

    @GetMapping("deleteOnlineCourseArrangement")
    public ReqResult del(Long id){
        System.out.println("id:"+id);
        return arrangementService.del(id) ? ReqResult.ok() : ReqResult.build(ResultEnum.delFail);
    }

//    @PostMapping("insertOnlineCourseArrangement") //@requestBody --> Json 不行，这个因为有个文件，就用
//    public ReqResult add(OnlineCourseArrangement onlineCourseArrangement, @RequestParam("file") MultipartFile file, HttpServletRequest request){
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
//            return ReqResult.build(ResultEnum.imageFormatError);
//        }
//        if(!strs[0].equals("image")){
//            return ReqResult.build(ResultEnum.imageFormatError);
//        }else if(!rs){
//            return ReqResult.build(ResultEnum.imageFormatError);
//        }
//
////        String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
//        String filePath = "imgupload/";
//        String uuid = UUID.randomUUID().toString();
////        String[] fileName_ = MultipartFileName.split(".");
////        if(fileName_.length != 2){
////            return ReqResult.build(ResultEnum.imageFormatError);
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
//        return arrangementService.add(onlineCourseArrangement)? ReqResult.ok():ReqResult.build(ResultEnum.insertFail);
//    }

}
