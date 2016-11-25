/*
 * Created by ShawnAnn on 16-11-25 上午11:06
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.util;

import android.os.Handler;
import android.view.View;

/**
 * 快速点击判断
 * Fast click check util
 *
 * @author ShawnAnn
 * @since 2016/4/21
 */
public class FastClickCheck {
    /**
     * 相同按钮点击必须间隔0.5s才能有效
     *
     * @param view 目标View
     */
    public static void check(final View view) {
        view.setClickable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, 500);
    }


    public static void check(final View view, int mills) {
        view.setClickable(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, mills);
    }
}
