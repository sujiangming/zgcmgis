package com.jdry.zhcm.beans;

/**
 * Created by JDRY-SJM on 2017/11/1.
 */

public class SaltBean {
    /**
     * salt : 125
     */
    public int status;
    public String message;
    public DataBean data;

    public static class DataBean {
        public String salt;
    }
}
