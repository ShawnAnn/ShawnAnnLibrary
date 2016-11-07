package com.shawnann.basic.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.shawnann.basic.util.SDKUtil;
import com.shawnann.basic.util.UserConfigUtil;

/**
 * 整体项目基础Activity
 * The baseActivity of the project
 *
 * @author ShawnAnn
 * @since 2016/9/29
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 系统版本大于等于4.4时将状态栏和导航栏透明显示
        if (SDKUtil.isAfter19()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            setTranslucentStatus(true);
        }


        // 忽略用户设置字体放大缩小
        UserConfigUtil.ignoreUserFont(this);
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 启动本Activity
     * lunch current activity
     *
     * @param context 上下文
     *                An Application {@link Context}.
     * @param clz     本Activity类对象 e.g. 比如被类中的BaseActivity.class
     *                current activity e.g. for example,BaseActivity.class {@link Class}.
     * @param bundle  传递参数,可为空
     *                params for start this activity which can be null.{@link Bundle}.
     */
    public static void actionStart(Context context, Class clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        if (bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
