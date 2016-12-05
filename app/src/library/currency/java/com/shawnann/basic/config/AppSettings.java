/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.config;

/**
 * app相关的配置文件
 * Created by ShawnAnn on 2016/10/13.
 */

public class AppSettings extends ConfigManager {

    private static class AppSettingsHolder {
        private static final AppSettings INSTANCE = new AppSettings();
    }

    public static final AppSettings getInstance() {
        return AppSettingsHolder.INSTANCE;
    }

    private AppSettings() {
        super(AppContext.getAppContext(), "app_settings");
    }

}
