/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawn.shawnannlibrary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.EditText;

import com.shawnann.basic.config.AppContext;
import com.shawnann.emoji.EmojiUtils;
import com.shawnann.emoji.Emojicon;
import com.shawnann.emoji.adapter.EmojiAdapter;
import com.shawnann.emoji.fragment.EmojiconGridFragment;
import com.shawnann.emoji.widgets.EmojiconEditText;

import java.util.ArrayList;
import java.util.List;


public class EmojiActivity extends FragmentActivity implements EmojiAdapter.OnEmojiChooseListener {


    EmojiconEditText wechat_message_content;
    private int position = -1;
    private boolean isSingle = false;
    private int chatType = 0x01;

    ViewPager message_emoji_viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.initAppContext(this);
        EmojiUtils.initEmoji();
        setContentView(R.layout.activity_wechat_message);
        init();
    }


   /* public void onUserEvent(ChatPersonChangeEvent chatPersonChangeEvent) {
        messageBean.person = chatPersonChangeEvent.person;
        messageBean.isPerson1 = chatPersonChangeEvent.isPerson1;
        Glide.with(AppContext.getAppContext()).load(messageBean.person.photo).crossFade().into(choose_person_image);
        choose_person_name.setText(messageBean.person.nickName);
    }


    public void onUserEvent(EmojiEvent emojiEvent) {
        if (emojiEvent.emojicon.icon.equals("file:///android_asset/screen_shot/emoji_back.png")) {
            // 删除
            backspace(wechat_message_content);
        } else {
            // 加入到EditText
            int start = wechat_message_content.getSelectionStart();
            int end = wechat_message_content.getSelectionEnd();
            if (start < 0) {
                wechat_message_content.append(newString(emojiEvent.emojicon.id));
            } else {
                wechat_message_content.getText().replace(Math.min(start, end), Math.max(start, end), newString(emojiEvent.emojicon.id), 0, newString(emojiEvent.emojicon.id).length());
            }
        }
    }*/

    public void backspace(EditText editText) {
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        editText.dispatchKeyEvent(event);
    }

    public String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }


    private void init() {
        wechat_message_content = (EmojiconEditText) findViewById(R.id.wechat_message_content);
        message_emoji_viewpager = (ViewPager) findViewById(R.id.message_emoji_viewpager);


        wechat_message_content.clearFocus();
        initEmoji();
    }


    List<EmojiconGridFragment> fragments = new ArrayList<>();
    EmojiPagerAdapter emojiPagerAdapter;

    private void initEmoji() {
        fragments.clear();
        List<Emojicon> allSmileyEmojicons = 0x01 == chatType ? EmojiUtils.getAllSmileyEmojicons() : EmojiUtils.getAllEmotionEmojicons();
        for (int i = 1; i <= ((allSmileyEmojicons.size() / 27) + 1); i++) {
            EmojiconGridFragment fragment = EmojiconGridFragment.newInstance(allSmileyEmojicons, i);
            fragment.setOnEmojiChooseListener(this);
            fragments.add(fragment);
        }

        emojiPagerAdapter = new EmojiPagerAdapter(getSupportFragmentManager(), fragments);
        message_emoji_viewpager.setAdapter(emojiPagerAdapter);
    }


    private void changeEmoji(boolean isQQ) {

        fragments.clear();
        List<Emojicon> allSmileyEmojicons = isQQ ? EmojiUtils.getAllSmileyEmojicons() : EmojiUtils.getEmojicons();
        for (int i = 1; i <= ((allSmileyEmojicons.size() / 27) + 1); i++) {
            fragments.add(EmojiconGridFragment.newInstance(allSmileyEmojicons, i));
        }
        if (emojiPagerAdapter == null) {
            emojiPagerAdapter = new EmojiPagerAdapter(getSupportFragmentManager(), fragments);
            message_emoji_viewpager.setAdapter(emojiPagerAdapter);
        } else {
            emojiPagerAdapter.update(fragments);
            message_emoji_viewpager.setCurrentItem(0);
        }

    }

    @Override
    public void onEmojiChoose(Emojicon emojicon) {
        if (emojicon.icon.equals("file:///android_asset/emoji/emoji_back.png")) {
            // 删除
            backspace(wechat_message_content);
        } else {
            // 加入到EditText
            int start = wechat_message_content.getSelectionStart();
            int end = wechat_message_content.getSelectionEnd();
            if (start < 0) {
                wechat_message_content.append(newString(emojicon.id));
            } else {
                wechat_message_content.getText().replace(Math.min(start, end), Math.max(start, end), newString(emojicon.id), 0, newString(emojicon.id).length());
            }
        }
    }
}
