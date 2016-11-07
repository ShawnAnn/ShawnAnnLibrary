package com.shawnann.basic.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 用户本地设置操作工具类
 * user local settings operate utils
 *
 * @author ShawnAnn
 * @since 2016/11/4
 */

public class UserConfigUtil {


    /**
     * 忽略用户设置的字体大小，完全使用程序中的大小规格
     * Ignore user's local font settings
     *
     * @param context 上下文  An Application {@link Context}.
     */
    public static void ignoreUserFont(Context context) {
        Configuration config = context.getResources().getConfiguration();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        config.fontScale = 1.0f;
        context.getResources().updateConfiguration(config, dm);
    }
}
