package com.shawnann.i18n.tran;


import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Strings {
    public static boolean isEmpty(String content) {
        return content == null || "".equals(content);
    }

    public static boolean Eq(String str1, String str2) {
       /* if (str1 == null || str2 == null)
            return false;
        return str1.equals(str2);*/

        return !(str1 == null || str2 == null) && str1.equals(str2);
    }

    /**
     * 读文件内容到String中
     *
     * @param name
     * @param charset
     * @return
     */
    public static String readStringFromAssets(Context context, String name, String charset, String failValue) {
        try {
            InputStream in = context.getAssets().open(name);
            return readStringFromStream(in, charset, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return failValue;
    }

    /**
     * 从文件中读取String
     *
     * @param filePath
     * @param charset
     * @param failValue
     * @return
     */
    public static String readStringFromFile(String filePath, String charset, String failValue) {
        File file = new File(filePath);
        if (file.exists() && file.isDirectory() && file.canRead()) {
            try {
                return readStringFromStream(new FileInputStream(file), charset, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return failValue;
    }

    /**
     * 从输入流中读取String
     *
     * @param in
     * @param charset
     * @return
     */
    public static String readStringFromStream(InputStream in, String charset, boolean autoclose) throws IOException {
        BufferedReader br = null;
        String line = null;
        StringBuilder content = new StringBuilder();
        try {
            br = new BufferedReader(new InputStreamReader(in, charset));
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } finally {
            if (autoclose && in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }


    /**
     * Returns the given string if it is non-null; the empty string otherwise.
     *
     * @param string the string to test and possibly return
     * @return {@code string} itself if it is non-null; {@code ""} if it is null
     */
    public static String nullToEmpty(String string) {
        return (string == null) ? "" : string;
    }


    /**
     * 截字符 串
     *
     * @param text
     * @param begin
     * @param len
     * @return
     */
    public static String subString(String text, int begin, int len) {
        if (begin < 0) {
            return "";
        }
        int end = Math.min(begin + len, text.length());
        return text.substring(begin, end);
    }
}

