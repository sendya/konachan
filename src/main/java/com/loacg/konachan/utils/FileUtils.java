package com.loacg.konachan.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Project: konachan
 * Author: Sendya <18x@loacg.com>
 * Time: 7/28/2016 1:03 AM
 */
public class FileUtils {


    /**
     * 获取网络资源文件名
     * @param path
     * @return String
     */
    public static String getFileName(String path) {
        if(StringUtils.isEmpty(path))
            return null;

        return path.substring( path.lastIndexOf("/")+1);
    }

    /**
     * 获取本地文件长度
     *
     * @param path
     * @return long
     */
    public static long getFileLength(String path) {
        long length = 0;
        File f = new File(path);
        if(f.exists() && f.isFile()){
            length = f.length();
        }
        return length;
    }

}
