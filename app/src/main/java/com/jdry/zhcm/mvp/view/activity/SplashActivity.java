package com.jdry.zhcm.mvp.view.activity;

import android.os.Bundle;

import com.jdry.zhcm.R;

/**
 * Created by JDRY-SJM on 2017/10/23.
 */

public class SplashActivity extends SjmBaseActivity {

    @Override
    public int getResouceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {
        setStatusBarTransparent();
        openNewActivity(MainActivity.class);
    }
}
