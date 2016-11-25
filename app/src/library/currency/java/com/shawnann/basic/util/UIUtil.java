package com.shawnann.basic.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * UI工具类
 * 获取各种与系统UI相关的参数
 *
 * @author Shawn
 * @since 2011-11-09
 */
public final class UIUtil {

    /**
     * 判断当前系统是否有底部虚拟导航栏
     *
     * @param context ...
     * @return ...
     */
    public static boolean hasSoftNavBar(Context context) {
        boolean hasSoftNavBar;
        try {
            int id = context.getResources()
                    .getIdentifier("config_showNavigationBar", "bool", "android");
            hasSoftNavBar = id > 0 && context.getResources().getBoolean(id);
        } catch (Resources.NotFoundException e) {
            if (SDKUtil.isAfter17()) {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Display d = wm.getDefaultDisplay();

                DisplayMetrics realMetrics = new DisplayMetrics();
                d.getRealMetrics(realMetrics);

                int realHeight = realMetrics.heightPixels;
                int realWidth = realMetrics.widthPixels;

                DisplayMetrics displayMetrics = new DisplayMetrics();
                d.getMetrics(displayMetrics);

                int displayHeight = displayMetrics.heightPixels;
                int displayWidth = displayMetrics.widthPixels;

                hasSoftNavBar = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
            } else {
                boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
                boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
                hasSoftNavBar = !hasMenuKey && !hasBackKey;
            }
        }
        return hasSoftNavBar;
    }
}