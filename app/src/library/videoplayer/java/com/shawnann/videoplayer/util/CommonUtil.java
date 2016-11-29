/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.videoplayer.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import com.shawnann.basic.config.AppContext;
import com.shawnann.basic.util.FileUtils;

import java.util.Formatter;
import java.util.Locale;

/**
 * 视频公共相关工具
 *
 * @author ShawnAnn
 * @since 2016/11/29
 */

public class CommonUtil {

    /**
     * 格式化时间展示
     * （时间限制在一日之内）
     *
     * @param timeMs 时长
     * @return 时间格式化字符串
     */
    public static String string4Time(int timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    /**
     * 下载速度文本
     *
     * @param speed 下载速度
     * @return
     */
    public static String getTextSpeed(long speed) {
        return FileUtils.formatFileSize(AppContext.getAppContext(), speed) + "/s";
    }


    /**
     * Get activity from context object
     *
     * @param context ...
     * @return object of Activity or null if it is not Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;

        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }

        return null;
    }
}
