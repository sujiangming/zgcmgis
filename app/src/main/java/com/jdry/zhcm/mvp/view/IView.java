package com.jdry.zhcm.mvp.view;

/**
 * Created by JDRY_SJM on 2017/4/17.
 */

public interface IView {
    void showProgress();

    void hideProgress();

    <T> void httpSuccessRender(T t, int order);

    <T> void httpFailureRender(T t, int order);

    void uploadSuccess(String filePath);

    void uploadFailure(String msg);
}
