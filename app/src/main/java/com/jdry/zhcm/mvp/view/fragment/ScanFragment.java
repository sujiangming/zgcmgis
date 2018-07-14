package com.jdry.zhcm.mvp.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.jdry.zhcm.R;
import com.jdry.zhcm.mvp.view.activity.ScanQRActivity;

import butterknife.OnClick;

/**
 * Created by JDRY_SJM on 2017/4/20.
 */

public class ScanFragment extends SjmBaseFragment {

    @Override
    public int getResourceId() {
        return R.layout.fragment_scan;
    }

    @Override
    protected void onCreateViewByMe(Bundle savedInstanceState) {

    }

    @OnClick(R.id.tv_scan)
    public void onViewClicked(View view) {
        toast("扫描二维码");
        openNewActivity(ScanQRActivity.class);
    }
}
