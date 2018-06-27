package com.jdry.zhcm.mvp.view.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.jdry.zhcm.utils.AppManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JDRY-SJM on 2017/10/30.
 */

public class LoginActivity extends SjmBaseActivity {
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPwd)
    EditText etPwd;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;

    private String userName;
    private String mPwd;

    private final static String TAG = LoginActivity.class.getName();

    @OnClick(R.id.btnLogin)
    public void btnClickLogin() {
        userName = etUserName.getText().toString();
        mPwd = etPwd.getText().toString();
        if (userName.isEmpty() || userName.length() <= 0) {
            toast("请输入用户名");
            return;
        }
        if (mPwd.isEmpty() || mPwd.length() <= 0) {
            toast("请输入密码");
            return;
        }
        showProgress();
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
        hideProgress();
    }

    @Override
    public <T> void httpFailureRender(T t, int order) {
        toast((String) t);
        hideProgress();
    }

    @Override
    public int getResouceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {

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
