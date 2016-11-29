/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawn.shawnannlibrary.app;

import android.app.Application;

import com.shawnann.basic.config.AppContext;
import com.shawnann.emoji.EmojiUtils;

/**
 * Created by ShawnAnn on 2016/11/29.
 */

public class ShawnAnnApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.initAppContext(this);
        EmojiUtils.initEmoji();
    }
}
