package com.shawnann.basic.util;


import com.shawnann.basic.config.AppContext;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源获取工具类
 * (依附于AppContext类)
 *
 * @author ShawnAnn
 * @since 2016/4/21.
 */
public final class ResUtil {
    private static final Map<Integer, Integer> COLORS = new HashMap<>();

    /**
     * 根据资源ID获取颜色值
     *
     * @param resID 资源ID
     * @return 颜色值 0xXXXXXXXX
     */
    public static int getColor(int resID) {
        if (COLORS.containsKey(resID)) return COLORS.get(resID);
        int color = AppContext.getAppContext().getResources().getColor(resID);
        COLORS.put(resID, color);
        return color;
    }


    public static int getDimen(int resID) {
        if (COLORS.containsKey(resID)) return COLORS.get(resID);
        int color = (int) AppContext.getAppContext().getResources().getDimension(resID);
        COLORS.put(resID, color);
        return color;
    }


}