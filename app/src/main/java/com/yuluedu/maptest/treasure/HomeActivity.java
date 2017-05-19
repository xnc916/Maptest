package com.yuluedu.maptest.treasure;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yuluedu.maptest.MainActivity;
import com.yuluedu.maptest.R;
import com.yuluedu.maptest.commons.ActivityUtils;
import com.yuluedu.maptest.treasure.list.TreasureListFragment;
import com.yuluedu.maptest.treasure.map.MapFragment;
import com.yuluedu.maptest.user.UserPrefs;
import com.yuluedu.maptest.user.account.AccountActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    private ImageView mInIcon;

    private MapFragment mMapFragment;
    private TreasureListFragment mListFragment;//列表视图
    private FragmentManager mFragmentManager;

    private ActivityUtils mActivityUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();
        mMapFragment = (MapFragment) mFragmentManager.findFragmentById(R.id.mapFragment);
        mActivityUtils = new ActivityUtils(this);

        //Toolbar
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            //不显示默认的标题,而显示布局中自己加的Textview
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // DrawerLayout的侧滑监听
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        toggle.syncState();//同步状态

        mDrawerLayout.addDrawerListener(toggle);

        // 侧滑菜单item的选择监听
        mNavigation.setNavigationItemSelectedListener(this);



        // 找到侧滑的头布局里面的头像
        mInIcon = (ImageView) mNavigation.getHeaderView(0).findViewById(R.id.iv_usericon);
        mInIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(HomeActivity.this,"头像",Toast.LENGTH_LONG).show();

                mActivityUtils.startActivity(AccountActivity.class);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //更新头像信息
        String photo = UserPrefs.getInstance().getPhoto();
        if (photo != null){
            //加载头像 使用Picasso
            //添加依赖Picasso
            Picasso.with(this).load(photo).into(mInIcon);
        }
    }

    // 侧滑的Navigation的Item每一项被选择的时候会触发
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            //埋藏宝藏
            case R.id.menu_hide:
                //Toast.makeText(this,"埋藏宝藏",Toast.LENGTH_LONG).show();
                mMapFragment.changeUIMode(2);//切换到埋藏宝藏的视图
                break;
            //退出
            case R.id.menu_logout:
                //Toast.makeText(this,"退出",Toast.LENGTH_LONG).show();
                //清空用户信息数据
                UserPrefs.getInstance().clearUser();
                //返回到main页面
                mActivityUtils.startActivity(MainActivity.class);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    // 准备工作：完成选项菜单的图标的切换等

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //item图标变化处理
        MenuItem item = menu.findItem(R.id.action_toggle);
        // 根据显示的视图不一样，设置不一样的图标
        if(mListFragment != null && mListFragment.isAdded()){
            item.setIcon(R.drawable.ic_map);
        }else {
            item.setIcon(R.drawable.ic_view_list);
        }


        return super.onPrepareOptionsMenu(menu);
    }

    // 创建：选项菜单的布局填充

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //菜单的填充
        getMenuInflater().inflate(R.menu.menu_home,menu);

        return true;
    }

    // 某一个选项菜单被选择的时候(点击)

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_toggle:
                //切换试图：地图的试图和列表的试图进行切换
                showListFragment();

                //更新选项菜单的视图：触发onPrepareOptionsMenu
                invalidateOptionsMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //显示隐藏列表视图
    private void showListFragment() {


        //                          展示状态
        if(mListFragment != null && mListFragment.isAdded()){
            //将list fragment弹出会退栈
            mFragmentManager.popBackStack();
            //移除listfragment
            mFragmentManager.beginTransaction().remove(mListFragment).commit();
            return;
        }

        mListFragment = new TreasureListFragment();

        //在布局中展示（FrameLayout作占位）
         mFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mListFragment)
                    //添加到回退栈
                 .addToBackStack(null)
                 .commit();
    }


    //处理返回键

    @Override
    public void onBackPressed() {
        //侧滑打开的，就先关闭
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            //如果MapFragment里面的视图是普通视图的话，可以退出
            if (mMapFragment.clickBackPressed()){
                super.onBackPressed();
            }

        }


    }
}
