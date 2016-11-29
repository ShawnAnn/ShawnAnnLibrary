/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.videoplayer.listener;

/**
 * 普通播放器回调
 */
public interface VideoAllCallBack {

    /**
     * 视频加载成功
     *
     * @param url     视频地址
     * @param objects ...
     */
    void onPrepared(String url, Object... objects);

    /**
     * 点击了开始按键播放
     *
     * @param url     视频地址
     * @param objects ...
     */
    void onClickStartIcon(String url, Object... objects);

    /**
     * 点击了错误状态下的开始按键
     *
     * @param url     视频地址
     * @param objects
     */
    void onClickStartError(String url, Object... objects);

    /**
     * 点击了播放状态下的开始按键--->停止
     *
     * @param url     视频地址
     * @param objects
     */
    void onClickStop(String url, Object... objects);

    /**
     * 点击了全屏播放状态下的开始按键--->停止
     *
     * @param url     视频地址
     * @param objects
     */
    void onClickStopFullscreen(String url, Object... objects);

    /**
     * 点击了暂停状态下的开始按键--->播放
     *
     * @param url     视频地址
     * @param objects
     */
    void onClickResume(String url, Object... objects);

    /**
     * 点击了全屏暂停状态下的开始按键--->播放
     *
     * @param url     视频地址
     * @param objects
     */
    void onClickResumeFullscreen(String url, Object... objects);

    /**
     * 点击了空白弹出seekbar
     *
     * @param url     视频地址
     * @param objects
     */
    void onClickSeekbar(String url, Object... objects);

    /**
     * 点击了全屏的seekbar
     *
     * @param url     视频地址
     * @param objects
     */
    void onClickSeekbarFullscreen(String url, Object... objects);

    /**
     * 播放完了
     *
     * @param url     视频地址
     * @param objects
     */
    void onAutoComplete(String url, Object... objects);

    /**
     * 进入全屏
     *
     * @param url     视频地址
     * @param objects
     */
    void onEnterFullscreen(String url, Object... objects);

    /**
     * 退出全屏
     *
     * @param url     视频地址
     * @param objects
     */
    void onQuitFullscreen(String url, Object... objects);

    /**
     * 进入小窗口
     *
     * @param url     视频地址
     * @param objects
     */
    void onQuitSmallWidget(String url, Object... objects);

    /**
     * 退出小窗口
     *
     * @param url     视频地址
     * @param objects
     */
    void onEnterSmallWidget(String url, Object... objects);

    /**
     * 触摸调整声音
     *
     * @param url     视频地址
     * @param objects
     */
    void onTouchScreenSeekVolume(String url, Object... objects);

    /**
     * 触摸调整进度
     *
     * @param url     视频地址
     * @param objects
     */
    void onTouchScreenSeekPosition(String url, Object... objects);

    /**
     * 触摸调整亮度
     *
     * @param url     视频地址
     * @param objects
     */
    void onTouchScreenSeekLight(String url, Object... objects);

}
