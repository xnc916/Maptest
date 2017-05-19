package com.yuluedu.maptest.user.login;

/**
 * Created by gameben on 2017-05-09.
 */

public interface LoginView {
    void showProgress();// 显示进度

    void hideProgress();// 隐藏进度

    void showMessage(String msg);// 显示信息

    void navigateToHome();// 跳转页面
}
