package com.jdry.zhcm.beans;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jdry.zhcm.global.JDRYDYApplication;
import com.jdry.zhcm.global.JDRYDYConstants;
import com.jdry.zhcm.utils.JdryPersistence;

import java.io.Serializable;

/**
 * Created by JDRY-SJM on 2017/11/9.
 */

public class LoginBean implements Serializable {

    private String password;
    private String username;
    private String token;
    private String cName;
    private String PhoneNum;

    private volatile static LoginBean instance = null;//volatile关键字来保证其线程间的可见性

    private LoginBean() {
    }

    public static LoginBean getInstance() {
        if (instance == null) {
            synchronized (LoginBean.class) {
                if (instance == null) {
                    instance = new LoginBean();
                }
            }
        }
        return instance;
    }

    public void load() {  // 加载本地数据
        try {
            String objectStr = JdryPersistence.getObject(JDRYDYApplication.getInstance().getApplicationContext(), JDRYDYConstants.LOGIN_INFO_SERIALIZE_KEY);
            if (null == objectStr || "".equals(objectStr)) {
                return;
            }
            LoginBean loginBean = JSON.parseObject(objectStr, LoginBean.class);
            saveLoginBean(loginBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLoginBean(LoginBean loginBean) {
        setInstance(loginBean);
        save();
    }

    public void save() {
        try {
            JdryPersistence.saveObject(JDRYDYApplication.getInstance().getApplicationContext(), JSON.toJSONString(this), JDRYDYConstants.LOGIN_INFO_SERIALIZE_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInstance(LoginBean loginBean) {
        this.setToken(loginBean.getToken());
        this.setUsername(loginBean.getUsername());
        this.setPassword(loginBean.getPassword());
        this.setcName(loginBean.getcName());
        this.setPhoneNum(loginBean.getPhoneNum());
    }

    public String getPassword() {
        return password;
    }

    public LoginBean setPassword(String password) {
        this.password = password;
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public LoginBean setUsername(String username) {
        this.username = username;
        return instance;
    }

    public String getToken() {
        return token;
    }

    public LoginBean setToken(String token) {
        this.token = token;
        return instance;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    @Override
    public String toString() {
        Log.e(LoginBean.class.getName(), JSON.toJSONString(this));
        return JSON.toJSONString(this);
    }
}
