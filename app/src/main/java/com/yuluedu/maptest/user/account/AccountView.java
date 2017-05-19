package com.yuluedu.maptest.user.account;

/**
 * Created by gameben on 2017-05-18.
 */

public interface AccountView {
    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void updatePhoto(String photoUrl);
}
