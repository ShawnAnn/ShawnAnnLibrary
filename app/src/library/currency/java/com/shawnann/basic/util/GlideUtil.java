/*
 * Created by ShawnAnn on 16-11-25 上午11:51
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.util;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shawnann.basic.config.AppContext;

/**
 * Glide 相关操作
 *
 * @author ShawnAnn
 * @since 2016/4/21
 */

public class GlideUtil {

    /**
     * 清理内存缓存
     *
     * @param activity
     */
    public static void clearMemoryCache(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.get(AppContext.getAppContext()).clearMemory();
            }
        });
    }

    /**
     * 清理内存缓存
     */
    public static void clearDiskCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(AppContext.getAppContext()).clearDiskCache();
            }
        }).start();
    }


    /**
     * Glide 配置在RecycleView上的滑动监听
     */
    public final static RecyclerView.OnScrollListener ON_SCROLL_LISTENER = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
            super.onScrollStateChanged(recyclerView, scrollState);
            switch (scrollState) {
                case RecyclerView.SCROLL_STATE_SETTLING:
                    Glide.with(AppContext.getAppContext()).pauseRequests();
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    Glide.with(AppContext.getAppContext()).resumeRequests();
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    Glide.with(AppContext.getAppContext()).resumeRequests();
                    break;
            }
        }
    };
}
