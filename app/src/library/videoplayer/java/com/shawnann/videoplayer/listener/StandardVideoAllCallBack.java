/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.videoplayer.listener;

public interface StandardVideoAllCallBack extends VideoAllCallBack {

    //点击了空白区域开始播放
    void onClickStartThumb(String url, Object... objects);

    //点击了播放中的空白区域
    void onClickBlank(String url, Object... objects);

    //点击了全屏播放中的空白区域
    void onClickBlankFullscreen(String url, Object... objects);

}
