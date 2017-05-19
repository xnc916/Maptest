package com.yuluedu.maptest;

/**
 * Created by gameben on 2017-05-09.
 */

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.yuluedu.maptest.user.UserPrefs;

/**
 * Created by gameben on 2017-04-12.
 *
 * 注意注册
 */

public class TreasureApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //用户仓库初始化
        UserPrefs.init(getApplicationContext());

        //百度地图SDk初始化
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());

    }
}
