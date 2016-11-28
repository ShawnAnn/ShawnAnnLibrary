/*
 * Created by ShawnAnn on 16-11-25 下午3:02
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.i18n.views;

import android.os.Parcel;
import android.widget.RemoteViews;

import com.shawnann.i18n.tran.I18N;

/**
 * Created by Shawn on 2015/7/23.
 */
public class I18NRemoteView extends RemoteViews {
    /**
     * Create a new RemoteViews object that will display the views contained
     * in the specified layout file.
     *
     * @param packageName Name of the package that contains the layout resource
     * @param layoutId    The id of the layout resource
     */
    public I18NRemoteView(String packageName, int layoutId) {
        super(packageName, layoutId);
    }

    /**
     * Reads a RemoteViews object from a parcel.
     *
     * @param parcel
     */
    public I18NRemoteView(Parcel parcel) {
        super(parcel);
    }

    @Override
    public void setTextViewText(int viewId, CharSequence text) {
        super.setTextViewText(viewId, I18N.convert(text));
    }
}

