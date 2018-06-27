package com.jdry.zhcm.mvp.model;

import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.mvp.model.inter.IModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by JDRY-SJM on 2017/11/1.
 */

public class ModelManager implements IModel<CommonBean> {

    public static Call<CommonBean> miBeanCall;
    public static Call<ResponseBody> miBeanCallResponseBody;
    public static ModelManager mDataManager;

    private ModelManager() {

    }

    public static ModelManager getInstance(Call<CommonBean> call, Call<ResponseBody> responseBody) {
        if (mDataManager == null) {
            synchronized (ModelManager.class) {
                mDataManager = new ModelManager();
            }
        }
        miBeanCall = call;
        miBeanCallResponseBody = responseBody;
        return mDataManager;
    }

    @Override
    public void getServerData(Callback<CommonBean> callback) {
        miBeanCall.enqueue(callback);
    }

    public void getServerDataForResponseBody(Callback<ResponseBody> callback) {
        miBeanCallResponseBody.enqueue(callback);
    }

}
