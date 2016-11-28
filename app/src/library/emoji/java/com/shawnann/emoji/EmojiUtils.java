/*
 * Created by ShawnAnn on 16-11-23 上午11:14
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 *  Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.emoji;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ShawnAnn on 2016/11/28.
 */

public class EmojiUtils {
    /**
     * 根据ID获取图片表情地址
     *
     * @param id
     * @return
     */
    public static String getEmojiStr(int id) {
        String s = "";
        if (!emojicons.isEmpty()) {
            for (Emojicon emojicon : emojicons) {
                if (emojicon.id == id) {
                    s = emojicon.icon;
                    return s;
                }
            }
        }

        if (!alipay_emojicons.isEmpty()) {
            for (Emojicon emojicon : alipay_emojicons) {
                if (emojicon.id == id) {
                    s = emojicon.icon;
                    return s;
                }
            }
        }

        if (!emojiconList.isEmpty()) {
            for (Emojicon emojicon : emojiconList) {
                if (emojicon.id == id) {
                    s = emojicon.icon;
                    return s;
                }
            }
        }
        return s;
    }

    public static boolean hasInited = false;

    /**
     * 应该在使用前进行初始化
     * 初始化表情资源
     */
    public static void initEmoji() {
        getAllEmotionEmojicons();
        getAllSmileyEmojicons();
        getEmojicons();
        hasInited = true;
    }

    // qq表情
    private static List<Emojicon> emojicons = new ArrayList<>();
    // 支付宝表情
    private static List<Emojicon> alipay_emojicons = new ArrayList<>();
    // emoji表情
    private static List<Emojicon> emojiconList = new ArrayList<>();


    public static List<Emojicon> getAllSmileyEmojicons() {
        if (emojicons.isEmpty()) {
            for (int i = 0; i < 100; i++) {
                String icon = "file:///android_asset/emoji/smiley/smiley_" + i + ".png";
                int id = 0x1f550 + i;
                Emojicon emojicon = new Emojicon(icon, id, "");
                emojicons.add(emojicon);
            }
        }
        return emojicons;
    }

    public static List<Emojicon> getAllEmotionEmojicons() {
        if (alipay_emojicons.isEmpty()) {
            for (int i = 1; i < 122; i++) {
                String index;
                if (i < 10) {
                    index = "0" + i;
                } else {
                    index = i + "";
                }
                String icon = "file:///android_asset/emoji/emotion/emotion_small_" + index + ".png";
                int id = 0x2f000 + i;
                Emojicon emojicon = new Emojicon(icon, id, "");
                alipay_emojicons.add(emojicon);
            }
        }
        return alipay_emojicons;
    }

    public static List<Emojicon> getEmojicons() {
        if (emojiconList.isEmpty()) {
            for (int i = 1; i < 50; i++) {
                String index;
                if (i < 10) {
                    index = "0" + i;
                } else {
                    index = i + "";
                }
                String icon = "file:///android_asset/emoji/emoji/emoji_1f6" + index + ".png";


                int id = 0x3faa0 + i;
                Emojicon emojicon = new Emojicon(icon, id, "");
                emojiconList.add(emojicon);
            }

        }
        return emojiconList;
    }
}
