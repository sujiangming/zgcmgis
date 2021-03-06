package com.jdry.zhcm.beans;

/**
 * Created by JDRY-SJM on 2017/11/2.
 */

public class CommonBean<T> {
    private int status;
    private String message;
    private T mData;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return mData;
    }

    public void setData(T mData) {
        this.mData = mData;
    }
}
