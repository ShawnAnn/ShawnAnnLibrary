/*
 * Created by ShawnAnn on 16-11-25 上午11:01
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.util;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * UI工具类
 * UI Tool
 * 获取各种与系统UI相关的参数
 * get any params about system ui
 *
 * @author ShawnAnn
 * @since 2011-11-09
 */
public final class UIUtil {

    /**
     * 判断当前系统是否有底部虚拟导航栏
     * whether this is a navigationBar
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

    /**
     * 复制文字到剪贴板
     * copy the text to clip bord
     *
     * @param context 上下文 ...
     * @param s       目标文字 target text
     * @param tip     提示文字 succeed tip
     */
    public static void saveStr2ClipBord(Context context, String s, String tip) {
        ClipboardManager cm = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本数据复制到剪贴板
        cm.setText(s);
        if (!TextUtils.isEmpty(tip)) {
            Toast.makeText(context, tip, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 开启软键盘
     *
     * @param context    上下文
     * @param targetView 目标View（一般为TextView）
     */
    public static void openSoftKeyborad(Context context, View targetView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(targetView, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     *
     * @param context    上下文
     * @param targetView 目标View（一般为TextView）
     */
    public static void hideSoftKeyboard(Context context, View targetView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(targetView.getWindowToken(), 0);
    }

    /**
     * 关闭软件盘
     *
     * @param activity 目标Activity
     */
    public static void closeSoftInputBord(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}