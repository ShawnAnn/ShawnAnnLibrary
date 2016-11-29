/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.videoplayer.bean;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 视频内部接收数据结构
 * Created by ShawnAnn on 2016/11/29.
 */

public class VideoModel {
    // 视频文件地址
    public String url = "";
    // 头文件信息
    public Map<String, String> mapHeadData = new HashMap<>();
    // 是否重复播放
    public boolean looping = false;
    // 播放倍速
    public float speed = 1;

    public VideoModel(String url, Map<String, String> mapHeadData, boolean looping, float speed) {
        this.url = url;
        this.mapHeadData = mapHeadData;
        this.looping = looping;
        this.speed = speed;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof VideoModel)) {
            return false;
        }
        VideoModel videoModel = (VideoModel) obj;
        return TextUtils.equals(videoModel.url, url);
    }
}
