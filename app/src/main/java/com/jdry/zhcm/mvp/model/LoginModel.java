package com.jdry.zhcm.mvp.model;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.beans.LoginBean;
import com.jdry.zhcm.beans.SaltBean;
import com.jdry.zhcm.global.JDRYDYConstants;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;
import com.jdry.zhcm.mvp.presenter.LoginPresenter;
import com.jdry.zhcm.utils.JDRYUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginModel {

    RetrofitUtil retrofitUtil = new RetrofitUtil();
    LoginPresenter presenter;
    Call call;

    public LoginModel(LoginPresenter presenter) {
        this.presenter = presenter;
    }

    public void getSalt(final String username, final String pwd) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        call = retrofitUtil.createReq(IService.class).getSalt(jsonObject.toJSONString());
        call.enqueue(new Callback<SaltBean>() {
            @Override
            public void onResponse(Call<SaltBean> call, Response<SaltBean> response) {
                if (null == response) {
                    presenter.getSaltFailure(JDRYDYConstants.HTTP_NO_NET_WORK);
                    return;
                }

                if (!response.isSuccessful()) {
                    presenter.getSaltFailure(JDRYDYConstants.HTTP_NO_NET_WORK);
                    return;
                }

                SaltBean saltBean = response.body();

                if (null == saltBean || 0 == saltBean.status || null == saltBean.data || TextUtils.isEmpty(saltBean.data.salt)) {
                    presenter.getSaltFailure(JDRYDYConstants.HTTP_NO_NET_WORK);
                    return;
                }

                String pwdMd5 = username + saltBean.data.salt + pwd;
                pwdMd5 = JDRYUtils.encrypt(pwdMd5);

                jsonObject.put("password", pwdMd5);

                login(jsonObject.toJSONString());
            }

            @Override
            public void onFailure(Call<SaltBean> call, Throwable t) {
                presenter.getSaltFailure(JDRYDYConstants.HTTP_REQUEST_NET_WORK_ERROR);
            }
        });
    }

    public void login(String data) {
        call = retrofitUtil.createReq(IService.class).login(data);
        call.enqueue(new Callback<CommonBean>() {
            @Override
            public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
                if (!response.isSuccessful()) {
                    presenter.httpRequestFailure(response.message());
                    return;
                }

                CommonBean commonBean = response.body();

                if (null == commonBean || 0 == commonBean.getStatus()) {
                    presenter.httpRequestFailure(commonBean.getMessage());
                    return;
                }

                if (null != commonBean.getData()) {
                    LoginBean loginBean = JSON.parseObject(commonBean.getData().toString(), LoginBean.class);
                    LoginBean.getInstance().saveLoginBean(loginBean);
                    //JDRYDYApplication.getDaoSession().getUserBeanDao().insertOrReplace(loginBean);
                    //LogUtils.d(LoginModel.class.getName(), JSON.toJSONString(JDRYDYApplication.getDaoSession().getLoginBeanDao().queryBuilder().unique()));
                }

                presenter.httpRequestSuccess(commonBean);
            }

            @Override
            public void onFailure(Call<CommonBean> call, Throwable t) {
                presenter.netWorkError(JDRYDYConstants.HTTP_REQUEST_NET_WORK_ERROR);
            }
        });
    }
}
