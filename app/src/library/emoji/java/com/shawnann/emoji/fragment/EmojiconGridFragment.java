/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.emoji.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.shawn.shawnannlibrary.R;
import com.shawnann.emoji.Emojicon;
import com.shawnann.emoji.adapter.EmojiAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ShawnAnn on 2016/10/28.
 */

public class EmojiconGridFragment extends Fragment {

    RecyclerView emoji_grids;
    List<Emojicon> emojicons;
    int page;
    EmojiAdapter.OnEmojiChooseListener listener;

    /**
     * 根据表情和页码生成表情
     *
     * @param emojicons
     * @param page
     * @return
     */
    public static EmojiconGridFragment newInstance(List<Emojicon> emojicons, int page) {
        EmojiconGridFragment emojiconGridFragment = new EmojiconGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("emojicons", (ArrayList<? extends Parcelable>) emojicons);
        bundle.putInt("page", page);
        emojiconGridFragment.setArguments(bundle);
        return emojiconGridFragment;
    }

    public void setOnEmojiChooseListener(EmojiAdapter.OnEmojiChooseListener listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.emojicon_grid, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        emojicons = bundle.getParcelableArrayList("emojicons");
        page = bundle.getInt("page");
        emoji_grids = (RecyclerView) view.findViewById(R.id.emoji_grids);
        emoji_grids.setLayoutManager(new GridLayoutManager(getContext(), 7));
        EmojiAdapter emojiAdapter = new EmojiAdapter(getContext(), emojicons, page, listener);
        emoji_grids.setAdapter(emojiAdapter);

    }

}
