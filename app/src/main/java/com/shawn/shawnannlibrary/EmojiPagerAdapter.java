/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawn.shawnannlibrary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.shawnann.emoji.fragment.EmojiconGridFragment;

import java.util.List;


/**
 * Created by ShawnAnn on 2016/10/28.
 */

public class EmojiPagerAdapter extends FragmentStatePagerAdapter {
    private List<EmojiconGridFragment> fragments;
    private FragmentManager fm;

    public EmojiPagerAdapter(FragmentManager fm, List<EmojiconGridFragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void update(List<EmojiconGridFragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}