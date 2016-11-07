package com.shawnann.basic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.shawnann.basic.util.UserConfigUtil;

/**
 * 整体项目的基础
 * The baseFragment of the project
 *
 * @author ShawnAnn
 * @since 2016/4/21.
 */

public class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserConfigUtil.ignoreUserFont(getContext());
    }
}
