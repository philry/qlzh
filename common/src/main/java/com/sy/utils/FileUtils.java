package com.sy.utils;

import org.springframework.web.multipart.MultipartFile;


/**
 * 文件操作工具类
 */
public class FileUtils {

    private FileUtils() {
    }


    /*
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(MultipartFile mf) {
        String filename = mf.getOriginalFilename();
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;


    }


    /*
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(MultipartFile mf) {
        String filename = mf.getOriginalFilename();
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;

    }


}

