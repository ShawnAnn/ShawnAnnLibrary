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
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
     * 获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /**
     * 获取导航栏的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int height = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
            height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 获取ActionBar高度
     *
     * @param activity activity
     * @return ActionBar高度
     */
    public static int getActionBarHeight(Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static void hideSupportActionBar(Context context, boolean actionBar, boolean statusBar) {
        if (actionBar) {
            if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                android.app.ActionBar ab = fragmentActivity.getActionBar();
                if (ab != null) {
                    ab.hide();
                }
            } else {
                ActionBar ab = getAppCompActivity(context).getSupportActionBar();
                if (ab != null) {
                    ab.setShowHideAnimationEnabled(false);
                    ab.hide();
                }
            }
        }
        if (statusBar) {
            if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                fragmentActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                getAppCompActivity(context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    }

    /**
     * Get AppCompatActivity from context
     *
     * @param context
     * @return AppCompatActivity if it's not null
     */
    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

    public static void showSupportActionBar(Context context, boolean actionBar, boolean statusBar) {
        if (actionBar) {
            if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                android.app.ActionBar ab = fragmentActivity.getActionBar();
                if (ab != null) {
                    ab.show();
                }
            } else {
                ActionBar ab = getAppCompActivity(context).getSupportActionBar();
                if (ab != null) {
                    ab.setShowHideAnimationEnabled(false);
                    ab.show();
                }
            }
        }

        if (statusBar) {
            if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                fragmentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                getAppCompActivity(context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    }


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