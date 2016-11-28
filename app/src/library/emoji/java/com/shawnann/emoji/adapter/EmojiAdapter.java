/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.emoji.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shawn.shawnannlibrary.R;
import com.shawnann.basic.config.AppContext;
import com.shawnann.emoji.Emojicon;


import java.util.ArrayList;
import java.util.List;


/**
 * 每一页展示4行，每一行7个表情，最后一个表情为返回键,页数从1开始
 * Created by ShawnAnn on 2016/10/28.
 */

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {

    public Context context;
    public List<Emojicon> emojicons = new ArrayList<>();
    public int currentPage = 0;
    private OnEmojiChooseListener listener;

    public EmojiAdapter(Context context, List<Emojicon> emojicons, int currentPage, OnEmojiChooseListener listener) {
        this.context = context;
        if (this.emojicons.isEmpty()) {
            this.emojicons.clear();
        }
        int start = (currentPage - 1) * 27;
        int end = (currentPage * 27);
        if (end > emojicons.size()) {
            end = emojicons.size();
        }
        // 加入一般表情
        this.emojicons.addAll(emojicons.subList(start, end));
        // 加入删除符号
        Emojicon emojicon = new Emojicon("file:///android_asset/emoji/emoji_back.png", 0x111111, "删除");
        this.emojicons.add(emojicon);
        this.currentPage = currentPage;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_emoji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Emojicon emojicon = emojicons.get(position);
        Glide.with(AppContext.getAppContext()).load(emojicon.icon).crossFade().into(holder.item_emoji_image);
        holder.item_emoji_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEmojiChoose(emojicon);
                }
            }
        });
    }


    public interface OnEmojiChooseListener {
        void onEmojiChoose(Emojicon emojicon);
    }


    @Override
    public int getItemCount() {
        return emojicons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_emoji_image;

        public ViewHolder(View itemView) {
            super(itemView);
            item_emoji_image = (ImageView) itemView.findViewById(R.id.item_emoji_image);
        }
    }
}
