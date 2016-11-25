package com.shawnann.basic.util;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nineton.wsgj.config.AppContext;

/**
 * Created by Shawn on 2016/9/29.
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
            // TODO Auto-generated method stub
            //每一条数据都是一个Map
            switch (scrollState) {
                case RecyclerView.SCROLL_STATE_SETTLING:
                    //  Log.i("Main", "用户在手指离开屏幕之前，由于滑了一下，视图仍然依靠惯性继续滑动");
                    Glide.with(AppContext.getAppContext()).pauseRequests();
                    //刷新
                    break;
                case RecyclerView.SCROLL_STATE_IDLE:
                    //  Log.i("Main", "视图已经停止滑动");
                    Glide.with(AppContext.getAppContext()).resumeRequests();
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    //  Log.i("Main", "手指没有离开屏幕，视图正在滑动");
                    Glide.with(AppContext.getAppContext()).resumeRequests();
                    break;
            }
            // Log.i("shawn", "当前滑动状态" + scrollState);
        }

    };
}
