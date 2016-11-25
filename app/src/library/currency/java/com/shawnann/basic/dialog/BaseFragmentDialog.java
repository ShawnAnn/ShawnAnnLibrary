package com.shawnann.basic.dialog;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;

import cn.nineton.wsgj.core.R;

/**
 *
 * Created by ShawnAnn on 2016/10/10.
 */

public class BaseFragmentDialog extends DialogFragment {

    public Dialog mDialog = null;

    private void ignoreUserFont() {
        Configuration config = getResources().getConfiguration();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        config.fontScale = 1.0f;
        getResources().updateConfiguration(config, dm);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.BottomInDialog);
        }
        Window window = mDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.BOTTOM);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        setCancelable(true);
        return mDialog;
    }
}
