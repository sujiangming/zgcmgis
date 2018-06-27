package com.jdry.zhcm.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.LoginBean;
import com.jdry.zhcm.mvp.view.custom.RichText;
import com.jdry.zhcm.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JDRY-SJM on 2017/12/4.
 */

public class AccountSetActivity extends SjmBaseActivity {
    @BindView(R.id.rtv_top_bar_back)
    RichText rtvTopBarBack;
    @BindView(R.id.tv_top_bar_title)
    TextView tvTopBarTitle;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    @Override
    public int getResouceId() {
        return R.layout.activity_account_set;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {
        setTopBar(this, tvTopBarTitle, "账号设置", rtvTopBarBack);
        LoginBean loginBean = LoginBean.getInstance();
        if (loginBean == null) {
            return;
        }
        tvAccount.setText(loginBean.getUsername() == null ? "未知" : loginBean.getUsername());
        tvUserName.setText(loginBean.getcName() == null ? "未知" : loginBean.getcName());
        tvAppVersion.setText("V" + Utils.getVersionName(this));
    }

    @OnClick({R.id.tv_account, R.id.tv_user_name, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_account:
                break;
            case R.id.tv_user_name:
                break;
            case R.id.tv_logout:
                openNewActivity(LoginActivity.class);
                break;
        }
    }
}
