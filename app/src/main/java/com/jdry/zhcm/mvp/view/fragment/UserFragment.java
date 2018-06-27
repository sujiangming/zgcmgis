package com.jdry.zhcm.mvp.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.LoginBean;
import com.jdry.zhcm.mvp.view.activity.AccountSetActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by JDRY_SJM on 2017/4/20.
 */

public class UserFragment extends SjmBaseFragment {

    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    @Override
    public int getResourceId() {
        return R.layout.fragment_user;
    }

    @Override
    protected void onCreateViewByMe(Bundle savedInstanceState) {
        LoginBean loginBean = LoginBean.getInstance();
        if (loginBean == null) {
            return;
        }
        tvAccount.setText(loginBean.getUsername() == null ? "未知" : loginBean.getUsername());
        tvUserName.setText(loginBean.getcName() == null ? "未知" : loginBean.getcName());
    }

    @OnClick({R.id.iv_set, R.id.rl_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_set:
                openNewActivity(AccountSetActivity.class);
                break;
            case R.id.rl_set:
                openNewActivity(AccountSetActivity.class);
                break;
        }
    }
}
