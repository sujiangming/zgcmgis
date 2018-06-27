package com.jdry.zhcm.beans;

import java.util.List;

/**
 * Created by JDRY-SJM on 2018/2/28.
 */

public class BaiduPositionBean {

    /**
     * status : 0
     * result : [{"x":23.157547,"y":113.42719},{"x":23.157547,"y":113.42719},{"x":23.157547,"y":113.42719},{"x":23.157547,"y":113.42719}]
     */

    private int status;
    private List<ResultBean> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * x : 23.157547
         * y : 113.42719
         */

        private double x;
        private double y;
        private int deviceStatus = -1;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public int getDeviceStatus() {
            return deviceStatus;
        }

        public void setDeviceStatus(int deviceStatus) {
            this.deviceStatus = deviceStatus;
        }
    }
}
