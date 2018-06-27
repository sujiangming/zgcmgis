package com.jdry.zhcm.beans;

/**
 * Created by JDRY-SJM on 2017/12/5.
 */

public class ApiParameter {
    private String userName;
    private String password;
    private int type;
    private int limit;//每页显示多少条数据
    private int offset;//忽略多少条数据
    private String search;//查询字符串，告警分页不要传入

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
