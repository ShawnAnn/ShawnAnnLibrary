/*
 * Created by ShawnAnn on 16-11-25 下午2:58
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.i18n.tran;

import android.content.Context;

import java.util.Locale;

/**
 * Created by Shawn on 14-8-16.
 */
public class I18N {


    private static I18NConvert sConvert = null;

    /**
     * 初始化
     *
     * @param context
     */
    public static void init(Context context) {
        CTSConverter.initConvert(context);
        updateConvert(context);
    }

    public static void updateConvert(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        if (locale.getCountry().equalsIgnoreCase("tw")) {
            sConvert = new S2TConvert();

        } else {
            sConvert = null;
        }
    }


    /**
     * 真正的转换
     *
     * @param text
     * @return
     */
    public static String convert(CharSequence text) {

        String rawString = text == null ? "" : text.toString();
        if (sConvert != null) {
            return sConvert.convert(rawString);
        }
        return rawString;
    }


    public static interface I18NConvert {
        String convert(String text);
    }

    public static class S2TConvert implements I18NConvert {

        @Override
        public String convert(String text) {
            return CTSConverter.S2T(text);
        }
    }


}
