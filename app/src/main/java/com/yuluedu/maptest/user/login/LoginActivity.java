package com.yuluedu.maptest.user.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.yuluedu.maptest.MainActivity;
import com.yuluedu.maptest.R;
import com.yuluedu.maptest.commons.ActivityUtils;
import com.yuluedu.maptest.commons.RegexUtils;
import com.yuluedu.maptest.custom.AlertDialogFragment;
import com.yuluedu.maptest.treasure.HomeActivity;
import com.yuluedu.maptest.user.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.btn_Login)
    Button mBtnLogin;


    private ActivityUtils mActivityUtils;
    private String mUsername;
    private String mPassword;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mActivityUtils = new ActivityUtils(this);
        ButterKnife.bind(this);

        // toolbar
        // Toolbar作为ActionBar展示
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.login);
        }

        //文本变化监听
        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPassword.addTextChangedListener(mTextWatcher);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        //变化前
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        //变化中
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        //变化后
        @Override
        public void afterTextChanged(Editable s) {
            mUsername = mEtUsername.getText().toString();
            mPassword = mEtPassword.getText().toString();

            // 判断用户名和密码都不为空，按钮才可以点击
            boolean canLogin = !(TextUtils.isEmpty(mUsername)||TextUtils.isEmpty(mPassword));
            mBtnLogin.setEnabled(canLogin);
        }
    };

    // 重写选项菜单的选中监听


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            // 处理ActionBar上面的返回箭头的事件
            case android.R.id.home:
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    @OnClick(R.id.btn_Login)
    public void onClick(){
        if(RegexUtils.verifyUsername(mUsername) != RegexUtils.VERIFY_SUCCESS){
            // 弹个对话框提示
            AlertDialogFragment.getInstances(getString(R.string.username_error),
                    getString(R.string.username_rules))
                    .show(getSupportFragmentManager(),"usernameError");
            return;
        }
        // 密码不符合规范
        if (RegexUtils.verifyPassword(mPassword) != RegexUtils.VERIFY_SUCCESS) {
            // 弹个对话框

            AlertDialogFragment.getInstances(getString(R.string.password_error),
                    getString(R.string.password_rules))
                    .show(getSupportFragmentManager(),"passwordError");
            return;
        }

        // 要去做登录的业务逻辑,模拟用户登录的场景，异步任务来模拟
        new LoginPresenter(this).login(new User(mUsername,mPassword));
    }




    // 进度条的显示
    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "登录", "正在登录中，请稍后~");

    }
    // 隐藏进度条
    @Override
    public void hideProgress() {
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }
    // 显示信息
    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    // 跳转页面
    @Override
    public void navigateToHome() {
        mActivityUtils.startActivity(HomeActivity.class);
        finish();

        //MainActivity是不是也需要关闭：发个本地广播的形式关闭
        Intent intent = new Intent(MainActivity.MAIN_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}

