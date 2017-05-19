package com.yuluedu.maptest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.yuluedu.maptest.commons.ActivityUtils;
import com.yuluedu.maptest.treasure.HomeActivity;
import com.yuluedu.maptest.user.UserPrefs;
import com.yuluedu.maptest.user.login.LoginActivity;
import com.yuluedu.maptest.user.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {
    public static final String MAIN_ACTION = "navigate_to_main";
    private ActivityUtils mActivityUtils;

    @BindView(R.id.btn_Register)
    Button btnRegister;
    @BindView(R.id.btn_Login)
    Button btnLogin;
    private Unbinder mUnbinder;

    //多个
//    @BindViews({R.id.button1, R.id.button2, R.id.button3})
//    Button bt;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);

        //判断用户是不是已经登陆
        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        //如果登陆了
        if (preferences != null){
            if (preferences.getInt("key_tokenid",0) == UserPrefs.getInstance().getTokenid()){
                mActivityUtils.startActivity(HomeActivity.class);
                finish();
            }
        }

        //注册本地广播
        IntentFilter fliter = new IntentFilter(MAIN_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,fliter);
    }


    @OnClick({R.id.btn_Register, R.id.btn_Login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_Register:
                mActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.btn_Login:
                mActivityUtils.startActivity(LoginActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
