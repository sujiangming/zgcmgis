package com.jdry.zhcm.beans;

import java.io.Serializable;

/**
 * Created by JDRY-SJM on 2017/12/5.
 */

public class AssetStatusBean implements Serializable{
    /**
     * A : 0
     * B : 0
     * C : 1
     * updateTime : 1520836167000
     */
    private String deviceNo;
    private String deviceId;
    private int deviceStatus;
    private int A;
    private int B;
    private int C;
    private long updateTime;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getA() {
        return A;
    }

    public void setA(int A) {
        this.A = A;
    }

    public int getB() {
        return B;
    }

    public void setB(int B) {
        this.B = B;
    }

    public int getC() {
        return C;
    }

    public void setC(int C) {
        this.C = C;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
}
