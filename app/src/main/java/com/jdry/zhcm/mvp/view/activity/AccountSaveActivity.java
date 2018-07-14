package com.jdry.zhcm.mvp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jdry.zhcm.R;
import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.beans.LoginBean;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JDRY-SJM on 2017/10/31.
 */

public class AccountSaveActivity extends SjmBaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.et_account)
    TextView etAccount;
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_confirm_new_pwd)
    EditText etConfirmNewPwd;

    private String userName = LoginBean.getInstance().getUsername();
    private String newPwd1;

    @Override
    public int getResouceId() {
        return R.layout.activity_account_save;
    }

    @Override
    protected void onCreateByMe(Bundle savedInstanceState) {
        etAccount.setText(userName);
    }

    @OnClick({R.id.iv_back, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                closeActivity();
                break;
            case R.id.tv_save:
                String oldPwd = etOldPwd.getText().toString();
                newPwd1 = etNewPwd.getText().toString();
                String newPwd2 = etConfirmNewPwd.getText().toString();
                if (oldPwd.equals("")) {
                    toast("请输入旧密码");
                    return;
                }
                if (newPwd1 == null || newPwd1.equals("")) {
                    toast("请输入新密码");
                    return;
                }
                if (newPwd2.equals("")) {
                    toast("请确认新密码");
                    return;
                }
                if (!newPwd1.equals(newPwd2)) {
                    toast("两次输入的新密码不一样");
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("username", LoginBean.getInstance().getUsername());
                jsonObject.put("oldPassword", oldPwd);
                jsonObject.put("newPassword", newPwd1);

                updatePwd(jsonObject.toJSONString());

                break;
        }
    }

    private void updatePwd(String data) {
        RetrofitUtil retrofitUtil = new RetrofitUtil();
        retrofitUtil.createReq(IService.class).updatePassword(data).enqueue(new Callback<CommonBean>() {
            @Override
            public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
                if (!response.isSuccessful()) {
                    toast(response.message());
                    return;
                }
                CommonBean commonBean = response.body();
                if (null == commonBean) {
                    toast(response.message());
                    return;
                }
                if (0 == commonBean.getStatus()) {
                    toast(commonBean.getMessage());
                    return;
                }
                confirmUpdatePwd(LoginBean.getInstance().getUsername());
            }

            @Override
            public void onFailure(Call<CommonBean> call, Throwable t) {
                t.getLocalizedMessage();
            }
        });
    }

    private void confirmUpdatePwd(String data) {
        RetrofitUtil retrofitUtil = new RetrofitUtil();
        retrofitUtil.createReq(IService.class).confirmUpdatePassword(data).enqueue(new Callback<CommonBean>() {
            @Override
            public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
                if (!response.isSuccessful()) {
                    toast(response.message());
                    return;
                }
                CommonBean commonBean = response.body();
                if (null == commonBean) {
                    toast(response.message());
                    return;
                }
                if (0 == commonBean.getStatus()) {
                    toast(commonBean.getMessage());
                    return;
                }
                toast(commonBean.getMessage());
            }

            @Override
            public void onFailure(Call<CommonBean> call, Throwable t) {
                toast(t.getLocalizedMessage());
            }
        });
    }
}
