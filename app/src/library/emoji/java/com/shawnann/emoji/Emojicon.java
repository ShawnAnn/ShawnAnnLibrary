/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.emoji;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ShawnAnn on 2016/10/28.
 */

public class Emojicon implements Parcelable {
    public String icon;

    public int id;

    public String emoji;

    public Emojicon(String icon, int id, String emoji) {
        this.icon = icon;
        this.id = id;
        this.emoji = emoji;
    }

    protected Emojicon(Parcel in) {
        icon = in.readString();
        id = in.readInt();
        emoji = in.readString();
    }

    public static final Creator<Emojicon> CREATOR = new Creator<Emojicon>() {
        @Override
        public Emojicon createFromParcel(Parcel in) {
            return new Emojicon(in);
        }

        @Override
        public Emojicon[] newArray(int size) {
            return new Emojicon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(icon);
        dest.writeInt(id);
        dest.writeString(emoji);
    }
}
