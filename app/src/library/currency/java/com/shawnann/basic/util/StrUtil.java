/*
 * Created by ShawnAnn on 16-11-23 上午11:16
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

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
public final class StrUtil {
    private static final Map<Integer, String> STRINGS = new HashMap<>();

    /**
     * 根据资源ID获取字符串
     *
     * @param resID 资源ID
     * @return 字符串 0xXXXXXXXX
     */
    public static String getString(int resID) {
        if (STRINGS.containsKey(resID)) return STRINGS.get(resID);
        String str = AppContext.getAppContext().getResources().getString(resID);
        STRINGS.put(resID, str);
        return str;
    }
}