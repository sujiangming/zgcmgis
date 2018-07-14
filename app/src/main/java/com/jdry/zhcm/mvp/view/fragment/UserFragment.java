package com.jdry.zhcm.mvp.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.jdry.zhcm.R;
import com.jdry.zhcm.mvp.view.activity.AboutActivity;
import com.jdry.zhcm.mvp.view.activity.AccountSaveActivity;
import com.jdry.zhcm.mvp.view.activity.LoginActivity;

import butterknife.OnClick;


/**
 * Created by JDRY_SJM on 2017/4/20.
 */

public class UserFragment extends SjmBaseFragment {

    @Override
    public int getResourceId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void onCreateViewByMe(Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_logout, R.id.ll_account_safe, R.id.ll_wish_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_logout:
                openNewActivity(LoginActivity.class);
                break;
            case R.id.ll_account_safe:
                openNewActivity(AccountSaveActivity.class);
                break;
            case R.id.ll_wish_report:
                openNewActivity(AboutActivity.class);
                break;
        }
    }
}
