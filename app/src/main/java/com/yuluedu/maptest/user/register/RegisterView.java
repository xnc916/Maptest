package com.yuluedu.maptest.user.register;

/**
 * Created by gameben on 2017/3/31.
 */

public interface RegisterView {
    void showProgress();// 显示进度

    void hideProgress();// 隐藏进度

    void showMessage(String msg);// 显示信息

    void navigateToHome();// 跳转页面

}
