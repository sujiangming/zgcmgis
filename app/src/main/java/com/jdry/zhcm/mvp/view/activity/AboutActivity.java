package com.jdry.zhcm.mvp.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.jdry.zhcm.R;
import com.jdry.zhcm.mvp.view.custom.RichText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JDRY-SJM on 2017/12/18.
 */

public class AboutActivity extends SjmBaseActivity {
    @BindView(R.id.rtv_top_bar_back)
    RichText rtvTopBarBack;
    @BindView(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;

    @Override
    public int getResouceId() {
        return R.layout.activity_about;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {
        setTopBar(this, tvTopBarTitle, "关于", rtvTopBarBack);
    }

    @OnClick(R.id.tv_update_version)
    public void onViewClicked() {
    }
}
