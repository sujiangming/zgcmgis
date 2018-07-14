package com.jdry.zhcm.mvp.presenter;


import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.mvp.model.LoginModel;
import com.jdry.zhcm.mvp.view.activity.LoginActivity;
import com.jdry.zhcm.mvp.view.activity.MainActivity;
import com.jdry.zhcm.mvp.view.activity.SjmBaseActivity;
import com.jdry.zhcm.mvp.view.activity.SplashActivity;

public class LoginPresenter implements IPresenter<CommonBean> {

    private LoginModel loginModel;
    private SjmBaseActivity activity;

    public LoginPresenter(SjmBaseActivity activity) {
        this.loginModel = new LoginModel(this);
        this.activity = activity;
    }

    public void getSalt(String username, String pwd) {
        activity.showProgress();
        loginModel.getSalt(username, pwd);
    }

    public void login(String data) {
        activity.showProgress();
        loginModel.login(data);
    }

    public void getSaltFailure(String msg) {
        activity.toast(msg);
    }

    @Override
    public void netWorkError(String netError) {
        activity.hideProgress();
        activity.toast(netError);
        if (activity instanceof SplashActivity) {
            activity.openNewActivity(LoginActivity.class);
        }
    }

    @Override
    public void httpRequestFailure(String noDataError) {
        activity.hideProgress();
        activity.toast(noDataError);
        if (activity instanceof SplashActivity) {
            activity.openNewActivity(LoginActivity.class);
        }
    }

    @Override
    public void httpRequestSuccess(CommonBean commonBean) {
        activity.hideProgress();
        if (activity instanceof LoginActivity) {
            activity.toast(commonBean.getMessage());
            LoginActivity loginActivity = (LoginActivity) activity;
            loginActivity.openNewActivity(MainActivity.class);
        } else {
            activity.openNewActivity(MainActivity.class);
        }

    }
}
