/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shawn on 2015/11/6.
 */
public abstract class ConfigManager {

    protected SharedPreferences sConfigSP = null;

    public ConfigManager(Context context, String configName) {
        sConfigSP = context.getSharedPreferences(configName, Context.MODE_PRIVATE);
    }

    public ConfigManager(Context context, String configName, int mode) {
        sConfigSP = context.getSharedPreferences(configName, Context.MODE_APPEND);
    }


    ///-----------------------------public method------------------------
    protected void setLong(String key, long value) {
        sConfigSP.edit().putLong(key, value).commit();
    }

    protected void setInt(String key, int value) {
        sConfigSP.edit().putInt(key, value).commit();
    }

    protected void setString(String key, String value) {
        sConfigSP.edit().putString(key, value).commit();
    }


    protected long getLong(String key, long defValue) {
        return sConfigSP.getLong(key, defValue);
    }


    protected int getInt(String key, int defValue) {
        return sConfigSP.getInt(key, defValue);
    }

    protected String getString(String key, String defValue) {
        return sConfigSP.getString(key, defValue);
    }


    protected boolean getBoolean(String key, boolean defValue) {
        return sConfigSP.getBoolean(key, defValue);
    }

    protected void setBoolean(String key, boolean value) {
        sConfigSP.edit().putBoolean(key, value).commit();
    }

}