package com.jdry.zhcm.mvp.presenter;

public interface IPresenter<T> {
    void netWorkError(String netError);

    void httpRequestFailure(String noDataError);

    void httpRequestSuccess(T t);
}
