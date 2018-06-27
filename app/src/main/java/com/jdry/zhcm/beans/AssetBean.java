package com.jdry.zhcm.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JDRY-SJM on 2017/12/5.
 */

public class AssetBean implements Serializable {
    /**
     * deviceId :
     * deviceNo :
     * deviceName : 测试杆塔1
     * deviceX : 108.4032974243164
     * deviceY : 26.542163848876953
     * createTime : 2018-03-09T22:29:22
     * updateTime : 2018-03-09T22:29:22
     * childrens : [{"deviceId":"","deviceNo":"","phaseNo":"3","deviceCardNO":"000100000035","alarmCount":14}]
     */

    private String deviceId;
    private String deviceNo;
    private String deviceName;
    private double deviceX;
    private double deviceY;
    private String createTime;
    private String updateTime;
    private List<ChildrensBean> childrens;
    private int deviceStatus;
    private int A = -1;
    private int B = -1;
    private int C = -1;

    public int getA() {
        return A;
    }

    public void setA(int a) {
        A = a;
    }

    public int getB() {
        return B;
    }

    public void setB(int b) {
        B = b;
    }

    public int getC() {
        return C;
    }

    public void setC(int c) {
        C = c;
    }



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<ChildrensBean> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<ChildrensBean> childrens) {
        this.childrens = childrens;
    }

    public static class ChildrensBean implements Serializable {
        /**
         * deviceId :
         * deviceNo :
         * phaseNo : 3
         * deviceCardNO : 000100000035
         * alarmCount : 14
         */

        private String deviceId;
        private String deviceNo;
        private int phaseNo;
        private String deviceCardNO;
        private String alarmCount;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public int getPhaseNo() {
            return phaseNo;
        }

        public void setPhaseNo(int phaseNo) {
            this.phaseNo = phaseNo;
        }

        public String getDeviceCardNO() {
            return deviceCardNO;
        }

        public void setDeviceCardNO(String deviceCardNO) {
            this.deviceCardNO = deviceCardNO;
        }

        public String getAlarmCount() {
            return alarmCount;
        }

        public void setAlarmCount(String alarmCount) {
            this.alarmCount = alarmCount;
        }
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
}
