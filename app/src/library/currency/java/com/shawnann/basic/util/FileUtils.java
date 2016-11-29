/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.shawnann.basic.config.AppContext;

import java.io.File;
import java.io.IOException;

/**
 * 文件操作相关工具类
 *
 * @author ShawnAnn
 * @since 2016/4/21
 */
public class FileUtils {

    /**
     * 是否存在SD卡
     *
     * @return
     */
    public static boolean hasSDCard() {
        boolean flag = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && Environment.getExternalStorageDirectory().exists();
        return flag;
    }

    /**
     * 获取手机可用空间大小字符串
     * 存在内存卡则返回为内存卡数据，否则为手机内存数据
     *
     * @param context 上下文
     * @return
     */
    public static String getLeftMemorySizeStr(Context context) {
        String memory;
        if (hasSDCard()) {
            // 获得sd卡的内存状态
            File sdcardFileDir = Environment.getExternalStorageDirectory();
            memory = getMemoryInfo(context, sdcardFileDir);
        } else {
            // 获得手机内部存储控件的状态
            File dataFileDir = Environment.getDataDirectory();
            memory = getMemoryInfo(context, dataFileDir);
        }
        return memory;
    }


    /**
     * 根据路径获取内存状态
     *
     * @param context 上下文
     * @param path    磁盘路径
     * @return
     */
    private static String getMemoryInfo(Context context, File path) {
        // 获得一个磁盘状态对象
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();   // 获得一个扇区的大小
        long totalBlocks = stat.getBlockCount();    // 获得扇区的总数
        long availableBlocks = stat.getAvailableBlocks();   // 获得可用的扇区数量
        // 总空间
        String totalMemory = Formatter.formatFileSize(context, totalBlocks * blockSize);
        // 可用空间
        String availableMemory = Formatter.formatFileSize(context, availableBlocks * blockSize);
        return "可用空间: " + availableMemory + "/总空间: " + totalMemory;
    }

    /**
     * 转换文件大小
     *
     * @param context 上下文
     * @param fileS   目标文件
     * @return
     */
    public static String formatFileSize(Context context, long fileS) {
        String fileSizeString = Formatter.formatFileSize(context, fileS);
        return fileSizeString;
    }

    /**
     * 默认存储的路径
     */
    private static final String NAME = AppContext.getAPPName();

    /**
     * 获取文件默认存储路径
     *
     * @return
     */
    public static String getDefaultPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(hasSDCard() ? Environment.getExternalStorageDirectory().getAbsolutePath() : AppContext.getAppContext().getFilesDir().getAbsolutePath());
        sb.append(File.separator);
        sb.append(NAME);
        sb.append(File.separator);

        String path = sb.toString();
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Throwable e) {
                path = "";
                e.printStackTrace();
            }
        }
        return path;
    }


    /**
     * 删除文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file != null) {
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                } else if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFile(files[i]);
                    }
                }
                file.delete();
            }
        }
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                String[] filePaths = file.list();
                for (String path : filePaths) {
                    deleteFile(filePath + File.separator + path);
                }
                file.delete();
            }
        }
    }


}
