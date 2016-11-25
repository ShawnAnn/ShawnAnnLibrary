/*
 * Created by ShawnAnn on 16-11-25 下午2:58
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.i18n.tran;

import java.util.Map;

/**
 * Created by Shawn on 2015/7/23.
 */
public class Collections {

    /**
     * 填充map
     *
     * @param map
     * @param keys
     * @param values
     */
    public static void fillMap(Map map, Object[] keys, Object[] values) {
        if (map == null)
            return;
        int count = Math.min(keys.length, values.length);

        /**
         * Count
         */
        for (int i = 0; i < count; i++) {
            map.put(keys[i], values[i]);
        }
    }
}
