package com.jdry.zhcm.mvp.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jdry.zhcm.R;
import com.jdry.zhcm.mvp.presenter.LoginPresenter;
import com.jdry.zhcm.utils.AppManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JDRY-SJM on 2017/10/30.
 */

public class LoginActivity extends SjmBaseActivity {

    @BindView(R.id.tv_map_title)
    TextView tvMapTitle;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.et_user_pwd)
    EditText etUserPwd;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    private String userName;
    private String mPwd;
    private LoginPresenter presenter;

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
        userName = etUserPhone.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            toast("用户名不能为空");
            return;
        }
        mPwd = etUserPwd.getText().toString();
        if (TextUtils.isEmpty(mPwd)) {
            toast("密码不能为空");
            return;
        }
        presenter.getSalt(userName, mPwd);
    }


    @Override
    public int getResouceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {
        presenter = new LoginPresenter(this);
    }

    @OnClick(R.id.tv_login)
    public void goMainActivity(View view) {
        openNewActivity(MainActivity.class);
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //两秒之内按返回键就会退出
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                toast("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().finishAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
