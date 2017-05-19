package com.yuluedu.maptest.user;

/**
 * Created by gameben on 2017-05-09.
 */

import com.google.gson.annotations.SerializedName;

/**
 * Created by gameben on 2017/3/31.
 *
 *
 * GsonFormat 创建实体类
 */

public class User {


    /**
     * UserName : qjd
     * Password : 654321
     */

    @SerializedName("UserName")
    private String userName;
    @SerializedName("Password")
    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

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

