/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.util;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 简单的网络请求类
 *
 * @author Shawn
 * @since 2016/4/28.
 */
public class WebUtils {

    /**
     * POST方式请求字符串
     *
     * @param url     请求链接地址
     * @param headers 请求头
     * @param params  请求参数
     * @return
     */
    public static String postString(String url, Map<String, String> headers, Map<String, String> params) {
        try {
            Response response = request("POST", url, headers, params);
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static Response getHeadString(String url, Map<String, String> headers, Map<String, String> params) {
        try {
            Response response = requestHeader("GET", url, headers, params);
            if (response.isSuccessful()) {
                return response;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * GET方式请求字符串
     *
     * @param url     请求链接地址
     * @param headers 请求头
     * @param params  请求参数
     * @return
     */
    public static String getString(String url, Map<String, String> headers, Map<String, String> params) {
        try {
            Response response = request("GET", url, headers, params);
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 请求网络数据
     *
     * @param method  请求方式 post/get
     * @param url     请求链接地址
     * @param headers 请求头文件
     * @param params  请求参数
     * @return
     * @throws IOException
     */
    private static Response request(String method, String url, Map<String, String> headers, Map<String, String> params) throws Exception {
        Request.Builder builder = new Request.Builder();
        if (headers != null)
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }

        String requestUrl = url;

        RequestBody mBody = null;
        if (params != null && !params.isEmpty()) {
            if (!"GET".equals(method)) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    bodyBuilder.add(param.getKey(), param.getValue());
                }
                mBody = bodyBuilder.build();
            } else {
                StringBuilder sb = new StringBuilder(url);
                if (url.indexOf("?") > 0 & !url.endsWith("&")) {
                    sb.append("&");
                } else if (url.indexOf("?") < 0) {
                    sb.append("?");
                }
                for (Map.Entry<String, String> param : params.entrySet()) {
                    try {
                        sb.append(URLEncoder.encode(param.getKey(), "UTF-8"))
                                .append('=')
                                .append(URLEncoder.encode(param.getValue(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new AssertionError(e);
                    }
                }
                requestUrl = sb.toString();
            }
        }

        builder.url(requestUrl);
        builder.method(method, mBody);
        Request request = builder.build();
        OkHttpClient client = new OkHttpClient();
        return client.newCall(request).execute();
    }


    /**
     * 只返回header信息
     *
     * @param method
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws IOException
     */
    private static Response requestHeader(String method, String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        Request.Builder builder = new Request.Builder();
        if (headers != null)
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder.addHeader(header.getKey(), header.getValue());
            }

        String requestUrl = url;

        RequestBody mBody = null;
        if (params != null && !params.isEmpty()) {
            if (!"GET".equals(method)) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    bodyBuilder.add(param.getKey(), param.getValue());
                }
                mBody = bodyBuilder.build();
            } else {
                StringBuilder sb = new StringBuilder(url);
                if (url.indexOf("?") > 0 & !url.endsWith("&")) {
                    sb.append("&");
                } else if (url.indexOf("?") < 0) {
                    sb.append("?");
                }
                for (Map.Entry<String, String> param : params.entrySet()) {
                    try {
                        sb.append(URLEncoder.encode(param.getKey(), "UTF-8"))
                                .append('=')
                                .append(URLEncoder.encode(param.getValue(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new AssertionError(e);
                    }
                }
                requestUrl = sb.toString();
            }
        }

        builder.url(requestUrl);
        builder.method(method, mBody);
        Request request = builder.head().build();
        OkHttpClient client = new OkHttpClient();
        return client.newCall(request).execute();
    }
}
