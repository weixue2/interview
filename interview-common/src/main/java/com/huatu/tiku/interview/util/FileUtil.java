package com.huatu.tiku.interview.util;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Author ZhenYang
 * @Date Created in 2018/1/14 16:48
 * @Description
 */
public class FileUtil {
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        System.out.println(targetFile.getPath());
        System.out.println("到这里没有");
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
}
