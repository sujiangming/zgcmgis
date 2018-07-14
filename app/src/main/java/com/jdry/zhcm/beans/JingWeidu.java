package com.jdry.zhcm.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JingWeidu {
    @Id(autoincrement = false)
    private Long id;
    @Unique
    private int uid;
    private Double x;
    private Double y;
    private String cityName;

    @Generated(hash = 516629921)
    public JingWeidu(Long id, int uid, Double x, Double y, String cityName) {
        this.id = id;
        this.uid = uid;
        this.x = x;
        this.y = y;
        this.cityName = cityName;
    }

    @Generated(hash = 16847133)
    public JingWeidu() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getUid() {
        return this.uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Double getX() {
        return this.x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return this.y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
