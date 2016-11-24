/*
 * Created by ShawnAnn on 16-11-23 上午11:16
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.web;


import android.database.Observable;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * 网络请求方法接口
 * 1、普通get请求{@link #executeGet(String, Map)}
 * 2、普通post请求{@link #executePost(String, Map)}
 * 3、多文件上传{@link #uploadFiles(String, String, Map)}
 */
public interface ApiService {


    /**
     * get请求
     *
     * @param url  请求链接地址
     * @param maps 请求参数
     * @return
     */
    @GET("{url}")
    Observable<ResponseBody> executeGet(
            @Path("url") String url,
            @QueryMap Map<String, String> maps);


    /**
     * post请求
     *
     * @param url  请求链接地址
     * @param maps 请求参数
     * @return
     */
    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            @QueryMap Map<String, String> maps);


    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Part("filename") String description,
            @PartMap() Map<String, RequestBody> maps);

}
