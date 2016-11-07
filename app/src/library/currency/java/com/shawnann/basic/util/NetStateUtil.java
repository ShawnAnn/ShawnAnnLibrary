package com.shawnann.basic.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.shawnann.basic.config.AppContext;


/**
 * 网络状态工具
 *
 * @author ShawnAnn
 * @since 2016/4/21.
 */
public class NetStateUtil {

    static final class NetType {
        public static final int INVALID = 0;
        public static final int WAP = 1;
        public static final int G2 = 2;
        public static final int G3 = 3;
        public static final int WIFI = 4;
        public static final int NO_WIFI = 5;
    }

    private String[] net_types = {};

    /**
     * 获取设备当前的网络状态
     *
     * @return 是否有网络
     */
    public static boolean isNetConnetced() {
        return getNetWorkType() != NetType.INVALID;
    }

    /**
     * 获取设备当前是否有wifi
     *
     * @return
     */
    public static boolean hasWifi() {
        return getNetWorkType() == NetType.WIFI;
    }


    /**
     * 得到当前网络类型名称
     *
     * @return
     */
    public static String getNetWorkTypeName() {
        ConnectivityManager manager = (ConnectivityManager) AppContext.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getTypeName();
        }
        return "未知";

    }

    /**
     * 获取网络类型
     *
     * @return 网络类型ID {@link NetType}
     */
    public static int getNetWorkType() {
        int type = NetType.INVALID;
        ConnectivityManager manager = (ConnectivityManager) AppContext.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String typeName = networkInfo.getTypeName();
            if (typeName.equalsIgnoreCase("WIFI")) {
                type = NetType.WIFI;
            } else if (typeName.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                type = TextUtils.isEmpty(proxyHost) ? (isFastMobileNetwork() ?
                        NetType.G3 : NetType.G2) :
                        NetType.WAP;
            }
        }
        return type;
    }

    /**
     * 判断是否是3G+的移动网络
     *
     * @return ...
     */
    public static boolean isFastMobileNetwork() {
        TelephonyManager telephonyManager = (TelephonyManager) AppContext.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * 获取活动的网络
     *
     * @return
     */
    public static NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) AppContext.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }
}
