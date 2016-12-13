/*
 * Created by ShawnAnn on 16-11-25 上午11:44
 * This is a personal tool library for usual coding,if you have any good idea,welcome pull requests.
 * My email : annshawn518@gamil.com
 * My QQ：1904508978
 * Copyright (c) 2016. All rights reserved.
 */

package com.shawnann.basic.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.shawn.shawnannlibrary.R;
import com.shawnann.basic.config.AppContext;
import com.shawnann.basic.util.UserConfigUtil;

import static com.shawnann.basic.util.UIUtil.getNavigationBarHeight;
import static com.shawnann.basic.util.UIUtil.getStatusBarHeight;


/**
 * * 整体项目的基础对话框
 * The baseFragmentDialog of the project
 *
 * @author ShawnAnn
 * @since 2016/4/21.
 */

public class BaseFragmentDialog extends DialogFragment {

    public Dialog mDialog = null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserConfigUtil.ignoreUserFont(getContext());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.BottomInDialog);
        }

        int screenHeight = (int) AppContext.getScreenHeight();
        int statusBarHeight = getStatusBarHeight(getContext());
        int navgateBarHeight = getNavigationBarHeight(getContext());
        int dialogHeight = screenHeight - statusBarHeight - navgateBarHeight;

        Window window = mDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        window.setGravity(Gravity.BOTTOM);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        setCancelable(true);
        return mDialog;
    }
}
