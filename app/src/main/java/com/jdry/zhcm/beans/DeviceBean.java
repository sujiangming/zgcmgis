package com.jdry.zhcm.beans;

import java.util.List;

public class DeviceBean {

    /**
     * data : [{"abc":"A","address":"都司路93号","charger":"王道银","devName":"医疗设备1","devNo":"6901028051996","devNumber":1,"devSpec":"12.1寸","devStatus":"0","groupMember":"韩顶煜","id":"1","installAdddress":"贵阳市第一人民医院","installArea":"3号综合楼","netPackage":"医疗","x":"106.652062549921","xzArea":"南明区","y":"26.6281412529427"},{"abc":"A","address":"都司路93号","charger":"卜小超","devName":"教育设备1","devNo":"3","devNumber":1,"devSpec":"12.1寸","devStatus":"1","groupMember":"韩顶煜","id":"2","installAdddress":"贵阳市第一人民医院","installArea":"外科综合楼左梯","netPackage":"教育","x":"106.652191805524","xzArea":"南明区","y":"26.6259022171583"},{"abc":"A","address":"都司路93号","charger":"苏江明","devName":"行政设备1","devNo":"234","devNumber":1,"devSpec":"26寸","devStatus":"2","groupMember":"韩顶煜","id":"234","installAdddress":"贵阳市第一人民医院","installArea":"门诊综合楼左梯1","netPackage":"行政","x":"106.65377224869","xzArea":"南明区","y":"26.6272884178192"},{"abc":"test","address":"test","charger":"test","devName":"test","devNo":"test","devNumber":1,"devSpec":"test","devStatus":"0","groupMember":"test","id":"402881ee646d8c4e01646dacf2ee0000","installAdddress":"test","installArea":"test","netPackage":"教育","x":"106.65377224869","xzArea":"test","y":"26.6272884178192"}]
     * message : 查询成功
     * status : 1
     */

    private String message;
    private int status;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * abc : A
         * address : 都司路93号
         * charger : 王道银
         * devName : 医疗设备1
         * devNo : 6901028051996
         * devNumber : 1
         * devSpec : 12.1寸
         * devStatus : 0
         * groupMember : 韩顶煜
         * id : 1
         * installAdddress : 贵阳市第一人民医院
         * installArea : 3号综合楼
         * netPackage : 医疗
         * x : 106.652062549921
         * xzArea : 南明区
         * y : 26.6281412529427
         */

        private String abc;
        private String address;
        private String charger;
        private String devName;
        private String devNo;
        private String devNumber;
        private String devSpec;
        private int devStatus;
        private String groupMember;
        private String id;
        private String installAdddress;
        private String installArea;
        private String netPackage;
        private Double x;
        private String xzArea;
        private Double y;

        public String getAbc() {
            return abc;
        }

        public void setAbc(String abc) {
            this.abc = abc;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCharger() {
            return charger;
        }

        public void setCharger(String charger) {
            this.charger = charger;
        }

        public String getDevName() {
            return devName;
        }

        public void setDevName(String devName) {
            this.devName = devName;
        }

        public String getDevNo() {
            return devNo;
        }

        public void setDevNo(String devNo) {
            this.devNo = devNo;
        }

        public String getDevNumber() {
            return devNumber;
        }

        public void setDevNumber(String devNumber) {
            this.devNumber = devNumber;
        }

        public String getDevSpec() {
            return devSpec;
        }

        public void setDevSpec(String devSpec) {
            this.devSpec = devSpec;
        }

        public int getDevStatus() {
            return devStatus;
        }

        public void setDevStatus(int devStatus) {
            this.devStatus = devStatus;
        }

        public String getGroupMember() {
            return groupMember;
        }

        public void setGroupMember(String groupMember) {
            this.groupMember = groupMember;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInstallAdddress() {
            return installAdddress;
        }

        public void setInstallAdddress(String installAdddress) {
            this.installAdddress = installAdddress;
        }

        public String getInstallArea() {
            return installArea;
        }

        public void setInstallArea(String installArea) {
            this.installArea = installArea;
        }

        public String getNetPackage() {
            return netPackage;
        }

        public void setNetPackage(String netPackage) {
            this.netPackage = netPackage;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public String getXzArea() {
            return xzArea;
        }

        public void setXzArea(String xzArea) {
            this.xzArea = xzArea;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }
    }
}
