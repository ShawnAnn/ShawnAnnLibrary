package com.shawnann.basic.util;

import android.util.Log;

import java.util.Locale;

/**
 * 应用于整个项目的LOG工具类
 * <p/>
 * LOG输出格式如下：
 * 日期 时间 PID-TID/包名 LOG级别/TAG标签头: 类全限定名->方法名->行号:<--->:具体MSG消息
 * 比如：
 * 11-09 15:26:00.492 29213-29213/com.shawn.test I/ShawnAnn: com.shawn.test.activities.MainActivity->onCreate->25:<--->:test msg
 * <p/>
 * Log util for the whole project
 * <p/>
 * The format of LOG like below:
 * date time PID-TID/package log grade/tag: class name->method name->line number:<--->:message
 * For example:
 * 11-09 15:26:00.492 29213-29213/com.shawn.test I/ShawnAnn: com.shawn.test.activities.MainActivity->onCreate->25:<--->:test msg
 *
 * @author ShawnAnn
 * @since 2016/4/21.
 */
public final class LogUtil {
    /**
     * LOG输出的标签头，可自由更改
     * <p/>
     * Tag of LOG, you can change as you like
     */
    private static final String TAG = "ShawnAnn";

    /**
     * 标题头格式
     * <p/>
     * Format of header
     */
    private static final String MATCH = "%s->%s->%d";

    /**
     * 连接LOG标题头和LOG信息的字符串
     * <p/>
     * Connector of LOG header and message string
     */
    private static final String CONNECTOR = ":<--->:";

    /**
     * LOG输出开关
     * 如果你启用了混淆并开启了代码优化则不需要更改此值
     * <p/>
     * The switch of LOG
     * If you enable proguard and code-optimize you will not change this value
     */
    private static boolean SWITCH = true;

    /**
     * 设置日志输出开关
     * Set the switch of LOG
     *
     * @param logUtilSwitch 是否开启日志
     */
    public static void setLogUtilSwitch(boolean logUtilSwitch) {
        SWITCH = logUtilSwitch;
    }

    private LogUtil() {
    }

    private static String buildHeader() {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
        return stack == null ? "UNKNOWN" : String.format(Locale.getDefault(), MATCH,
                stack.getClassName(), stack.getMethodName(), stack.getLineNumber());
    }

    /**
     * 打印出调用该方法所对应的标题头
     */
    public static void v() {
        if (SWITCH) Log.v(TAG, buildHeader());
    }

    /**
     * 打印出调用该方法所对应的标题头和LOG消息
     *
     * @param msg ...
     */
    public static void v(Object msg) {
        if (SWITCH) Log.v(TAG, buildHeader() + CONNECTOR + msg.toString());
    }

    /**
     * @see #v()
     */
    public static void d() {
        if (SWITCH) Log.d(TAG, buildHeader());
    }

    /**
     * @see #v(Object)
     */
    public static void d(Object msg) {
        if (SWITCH) Log.d(TAG, buildHeader() + CONNECTOR + msg.toString());
    }

    /**
     * @see #v()
     */
    public static void i() {
        if (SWITCH) Log.i(TAG, buildHeader());
    }

    /**
     * @see #v(Object)
     */
    public static void i(Object msg) {
        if (SWITCH) Log.i(TAG, buildHeader() + CONNECTOR + msg.toString());
    }

    /**
     * @see #v()
     */
    public static void w() {
        if (SWITCH) Log.w(TAG, buildHeader());
    }

    /**
     * @see #v(Object)
     */
    public static void w(Object msg) {
        if (SWITCH) Log.w(TAG, buildHeader() + CONNECTOR + msg.toString());
    }

    /**
     * @see #v()
     */
    public static void e() {
        if (SWITCH) Log.e(TAG, buildHeader());
    }

    /**
     * @see #v(Object)
     */
    public static void e(Object msg) {
        if (SWITCH) Log.e(TAG, buildHeader() + CONNECTOR + msg.toString());
    }
}