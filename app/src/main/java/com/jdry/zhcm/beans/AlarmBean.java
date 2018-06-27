package com.jdry.zhcm.beans;

import java.io.Serializable;

/**
 * Created by JDRY-SJM on 2017/12/5.
 */

public class AlarmBean implements Serializable{

    /**
     * {
     * alertId            // 报警id
     * deviceNo:             // 设备id
     * deviceName:           // 名称
     * deviceX:              // 经度
     * deviceY:              // 维度
     * userName:       // 负责人
     * phone:          // 负责人电话
     * alertTime:           // 告警时间
     * }
     */
    private String alertId;
    private String deviceNo;
    private String deviceName;
    private double deviceX;
    private double deviceY;
    private String userName;
    private String phone;
    private long alertTime;
    private String phaseName;

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public double getDeviceX() {
        return deviceX;
    }

    public void setDeviceX(double deviceX) {
        this.deviceX = deviceX;
    }

    public double getDeviceY() {
        return deviceY;
    }

    public void setDeviceY(double deviceY) {
        this.deviceY = deviceY;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(long alertTime) {
        this.alertTime = alertTime;
    }
}
