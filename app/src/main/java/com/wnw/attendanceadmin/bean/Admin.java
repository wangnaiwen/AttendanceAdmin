package com.wnw.attendanceadmin.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by wnw on 2017/4/19.
 */

public class Admin extends BmobObject {
    private String userName;
    private String password;

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
}
