package com.jdry.zhcm.mvp.view.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.ApiParameter;
import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.beans.LoginBean;
import com.jdry.zhcm.global.JDRYDYConstants;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;
import com.jdry.zhcm.mvp.presenter.PresenterManager;

/**
 * Created by JDRY-SJM on 2017/10/23.
 */

public class SplashActivity extends SjmBaseActivity {

    private LoginBean loginBean;
    private String userName;
    private String mPwd;


    @Override
    public int getResouceId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {
        setStatusBarTransparent();
        initData();
    }

    public void initData() {
        loginBean = LoginBean.getInstance();
        if (null == loginBean) {
            openNewActivity(LoginActivity.class);
            return;
        }
        userName = loginBean.getUsername();
        if (TextUtils.isEmpty(userName)) {
            openNewActivity(LoginActivity.class);
            return;
        }
        mPwd = loginBean.getPassword();

        if (TextUtils.isEmpty(mPwd)) {
            openNewActivity(LoginActivity.class);
            return;
        }
        ApiParameter userInfo = new ApiParameter();
        userInfo.setUserName(userName);
        userInfo.setPassword(mPwd);
        PresenterManager.getInstance()
                .setmIView(this)
                .setCall(RetrofitUtil.getInstance().createReq(IService.class).login(userInfo))
                .request(JDRYDYConstants.INVOKE_API_DEFAULT_TIME);
    }

    @Override
    public <T> void httpSuccessRender(T t, int order) {
        CommonBean commonBean = (CommonBean) t;
        JSONObject object = JSON.parseObject(commonBean.getData().toString());
        String token = object.getString("token");
        LoginBean.getInstance().setUsername(userName);
        LoginBean.getInstance().setPassword(mPwd);
        LoginBean.getInstance().setToken(token);
        LoginBean.getInstance().setPhoneNum(object.getString("PhoneNum"));
        LoginBean.getInstance().setcName(object.getString("CName"));
        LoginBean.getInstance().save();
        openNewActivity(MainActivity.class);
    }

    @Override
    public <T> void httpFailureRender(T t, int order) {
        toast((String) t);
        openNewActivity(LoginActivity.class);
    }
}
