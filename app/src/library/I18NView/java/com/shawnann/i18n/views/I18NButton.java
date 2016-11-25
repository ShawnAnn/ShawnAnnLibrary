/*
 * Created by ShawnAnn on 16-11-25 下午2:58
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.i18n.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.shawnann.i18n.tran.I18N;
import com.youloft.calendar.almanac.tran.I18N;

/**
 * Created by Shawn on 2015/7/23.
 */
public class I18NButton extends Button {
    public I18NButton(Context context) {
        super(context);
    }

    public I18NButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public I18NButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(I18N.convert(text), type);
    }
}
