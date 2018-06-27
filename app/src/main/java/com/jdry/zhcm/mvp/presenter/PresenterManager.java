package com.jdry.zhcm.mvp.presenter;

import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.mvp.model.ModelManager;
import com.jdry.zhcm.mvp.view.IView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JDRY-SJM on 2017/11/2.
 */

public class PresenterManager {
    private IView mIView;
    private static PresenterManager mInstance;
    private Call mCall;
    private int mOrder = 1;//默认每次只是调用一次接口
    private String errorInfo = "No Data Return !";


    private PresenterManager() {

    }

    public static PresenterManager getInstance() {
        if (mInstance == null) {
            synchronized (PresenterManager.class) {
                mInstance = new PresenterManager();
            }
        }
        return mInstance;
    }

    public PresenterManager setmIView(IView mIView) {
        this.mIView = mIView;
        return mInstance;
    }

    public PresenterManager setCall(Call mCall) {
        this.mCall = mCall;
        return mInstance;
    }

    public PresenterManager request() {
        ModelManager.getInstance(mCall, null).getServerData(new Callback<CommonBean>() {
            @Override
            public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
                if (response.isSuccessful()) {
                    CommonBean commonBean = response.body();
                    render(commonBean);
                } else {
                    mIView.httpFailureRender(response.message(), mOrder);
                }
            }

            @Override
            public void onFailure(Call<CommonBean> call, Throwable t) {
                mIView.httpFailureRender(t.getMessage(), mOrder);
            }
        });
        return mInstance;
    }

    /**
     * 针对在一个页面有多次调用不同接口的情况下（与调用顺序无关）
     * 需要输入一个调用标识
     *
     * @param flag
     * @return
     */
    public PresenterManager request(int flag) {
        final int invokeFlag = flag;
        ModelManager.getInstance(mCall, null).getServerData(new Callback<CommonBean>() {
            @Override
            public void onResponse(Call<CommonBean> call, Response<CommonBean> response) {
                CommonBean commonBean = response.body();
                render(commonBean, invokeFlag);
            }

            @Override
            public void onFailure(Call<CommonBean> call, Throwable t) {
                mIView.httpFailureRender(t.getMessage(), invokeFlag);
            }
        });
        return mInstance;
    }

    /**
     * 这个回调方法，针对在一个页面有多次调用不同接口的情况下
     * 需要输入一个调用标识
     *
     * @param commonBean
     * @param flag
     */
    private void render(CommonBean commonBean, int flag) {
        if (commonBean == null) {
            mIView.httpFailureRender(errorInfo, flag);
            return;
        }
        if (commonBean.getStatus() == 1) {
            mIView.httpSuccessRender(commonBean, flag);
        } else {
            mIView.httpFailureRender(commonBean.getMessage(), flag);
        }
    }

    private void render(CommonBean commonBean) {
        if (commonBean == null) {
            mIView.httpFailureRender(errorInfo, mOrder);
            return;
        }
        if (commonBean.getStatus() == 1) {
            mIView.httpSuccessRender(commonBean, mOrder);
        } else {
            mIView.httpFailureRender(commonBean.getMessage(), mOrder);
        }
    }
}
