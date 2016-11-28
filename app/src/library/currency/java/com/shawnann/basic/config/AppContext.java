

/*
 * Created by ShawnAnn on 16-11-23 上午11:15
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.config;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 获取系统相关信息
 * get information about current App
 * 使用之前必须现在Application文件中调用{@link #initAppContext(Context)}方法
 * you must call {@link #initAppContext(Context)} before you use the methods of this class
 *
 * @author ShawnAnn
 * @since 2016/4/21.
 */
public class AppContext {

    private static Context context;
    private static DisplayMetrics mDisplayMetrics = null;
    public static final int SDK_VERSION = Build.VERSION.SDK_INT;
    private static PackageInfo mPackageInfo;

    public static void initAppContext(Context context1) {
        context = context1;
    }

    public static Context getAppContext() {
        return context;
    }


    /**
     * 得到版本信息
     * get the package information
     *
     * @return
     */
    public static PackageInfo getPackageInfo() {
        if (mPackageInfo == null) {
            try {
                mPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mPackageInfo;
    }


    /**
     * 是在之前某个版本
     * whether before target sdkApi
     *
     * @param sdkApi 目标sdk版本
     *               target sdkApi
     */
    public static boolean beforeSDK(int sdkApi) {
        return SDK_VERSION < sdkApi;
    }


    /**
     * 获取屏幕尺寸
     * get the screen size
     *
     * @param context 上下文
     *                An Application {@link Context}.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public synchronized static DisplayMetrics getDisplayMetrics(Context context) {
        if (mDisplayMetrics == null) {
            mDisplayMetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (beforeSDK(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
                wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
            } else {
                wm.getDefaultDisplay().getRealMetrics(mDisplayMetrics);
            }
        }
        return mDisplayMetrics;
    }

    /**
     * 获取屏幕宽度
     * get width of the screen
     *
     * @return 返回浮点类型的屏幕宽度
     * screen width as float
     */
    public static float getScreenWidth() {
        return getDisplayMetrics(context).widthPixels;
    }

    /**
     * 获取屏幕高度
     * get height of the screen
     *
     * @return 返回浮点类型的屏幕高度
     * screen height as float
     */
    public static float getScreenHeight() {
        return getDisplayMetrics(context).heightPixels;
    }

    /**
     * 得到版本名称
     * get the version name of the app
     *
     * @return 版本名称的字符串
     * the string of the version name
     */
    public static String getVersionName() {
        getPackageInfo();
        if (mPackageInfo != null)
            return mPackageInfo.versionName;
        return "未知";
    }


    /**
     * 得到应用程序名称
     * get the name of the application
     *
     * @return 应用名称
     * application name
     */
    public static String getAPPName() {
        getPackageInfo();
        if (mPackageInfo != null) {
            return mPackageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
        }
        return "未知";
    }

    /**
     * 得到系统名称
     * get current os
     *
     * @return just return Android OS
     */
    public static String getSystemName() {
        // return System.getProperty("os.name");
        return "Android OS";
    }

    /**
     * 获取android系统版本号
     * get android system version code
     *
     * @return 系统版本号, 比如4.3、6.0、7.0等
     * system version code like 4.3...
     */
    public static String getSystemVersionCode() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备型号
     * get the mobile name
     *
     * @return 设备型号, 比如HUAWEI P9
     */
    public static String getMobileName() {
        return Build.MODEL;
    }

    /**
     * 得到运营商名字(中国)
     * get the sim operator information(China)
     *
     * @return
     */
    public static String getSimOperatorInfo() {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operatorString = telephonyManager.getSimOperator();
        if (operatorString == null) {
            return "未知";
        }
        if (operatorString.equals("46000") || operatorString.equals("46002")) {
            return "中国移动";
        } else if (operatorString.equals("46001")) {

            return "中国联通";
        } else if (operatorString.equals("46003")) {
            return "中国电信";
        } else {
            return "未知";
        }
    }

    /**
     * 得到手机唯一码
     * 此处唯一码为设备号和Mac地址组合
     * get the unicode of the cell phone
     * use deviceID and MacAddress
     *
     * @return
     */
    public static String getIDFA(Context context) {
        //imei值
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        // MAC值
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
        return "android:" + szImei + m_szWLANMAC;
    }


}

