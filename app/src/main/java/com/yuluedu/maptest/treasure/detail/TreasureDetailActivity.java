package com.yuluedu.maptest.treasure.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.yuluedu.maptest.R;
import com.yuluedu.maptest.commons.ActivityUtils;
import com.yuluedu.maptest.custom.TreasureView;
import com.yuluedu.maptest.treasure.Treasure;
import com.yuluedu.maptest.treasure.map.MapFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TreasureDetailActivity extends AppCompatActivity implements TreasureDetailView{
    private static final String KEY_TREASURE = "key_treasure";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.detail_treasure)
    TreasureView mTreasureView;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetailDescription;
    private Treasure mTreasure;

    private ActivityUtils mActivityUtils;
    private TreasureDetailPresenter mDetailPresenter;

    /**
     * 对外提供一个方法，跳转到本页面
     * 规范一下传递的数据：需要什么参数就必须要传入
     */
    public static void open(Context context, Treasure treasure) {
        Intent intent = new Intent(context, TreasureDetailActivity.class);
        intent.putExtra(KEY_TREASURE, treasure);
        context.startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        mDetailPresenter = new TreasureDetailPresenter(this);

        mTreasure = (Treasure) getIntent().getSerializableExtra(KEY_TREASURE);

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            //设置标题和返回箭头
            getSupportActionBar().setTitle(mToolbar.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //地图展示
        initMapView();

        //宝藏信息卡片的展示
        mTreasureView.bindTreasure(mTreasure);

        // 网络获取宝藏的详情数据
        TreasureDetail treasureDetail = new TreasureDetail(mTreasure.getId());
        mDetailPresenter.getTreasureDetail(treasureDetail);
    }

    //地图展示
    private void initMapView() {

        //宝藏位置
        LatLng latLng = new LatLng(mTreasure.getLatitude(),mTreasure.getLongitude());

        MapStatus mapStatus = new MapStatus.Builder()
                .target(latLng)
                .zoom(18)
                .rotate(0)
                .overlook(-20)
                .build();

        //只展示，没有操作
        BaiduMapOptions options = new BaiduMapOptions()
                .mapStatus(mapStatus)
                .compassEnabled(false)
                .scaleControlEnabled(false)
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(false)
                .rotateGesturesEnabled(false);

        // 创建的地图控件
        MapView mapView = new MapView(this,options);

        //放到布局中
        mFrameLayout.addView(mapView);

        //拿到地图的操作类
        BaiduMap map = mapView.getMap();

        //添加一个覆盖物
        BitmapDescriptor dot_expand = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);
        MarkerOptions option = new MarkerOptions()
                .position(latLng)
                .icon(dot_expand)
                .anchor(0.5f,0.5f);
        map.addOverlay(option);

    }

    //处理返回箭头事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //toolbar 的图标点击事件
    @OnClick(R.id.iv_navigation)
    public void showPopupMenu(View view){
        //展示出来一个PopupMenu
        /**
         * 1.创建出一个弹出式菜单
         * 2.菜单项的填充（布局）
         * 3.设置每一个菜单项的点击监听
         * 4.要显示PopupMenu
         */
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.inflate(R.menu.menu_navigation);
        popupMenu.setOnMenuItemClickListener(mMenuItemClickListener);
        popupMenu.show();

    }
    private PopupMenu.OnMenuItemClickListener mMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        //点击菜单项会触发：具体根据item的id来判断
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            // 不管进行骑行还是步行，都需要起点和终点：坐标和地址
            // 起点：我们定位的位置和地址
            LatLng start = MapFragment.getMyLocation();
            String startAddr = MapFragment.getLocationAddr();

            //终点：宝藏的位置和地址
            LatLng end = new LatLng(mTreasure.getLatitude(),mTreasure.getLongitude());
            String endAddr = mTreasure.getLocation();





            //步行骑行
            switch(item.getItemId()){
                case R.id.walking_navi:
                    //步行
                    starWalkingNavi(start,startAddr,end,endAddr);
                    break;
                case R.id.biking_navi:
                    //骑行
                    startBikingNavi(start,startAddr,end,endAddr);
                    break;
            }

            return false;
        }
    };

    //骑行导航
    private void startBikingNavi(LatLng start, String startAddr, LatLng end, String endAddr) {
        NaviParaOption option = new NaviParaOption()
                .startName(startAddr)
                .startPoint(start)
                .endName(endAddr)
                .endPoint(end)
                ;
        boolean bikeNavi = BaiduMapNavigation.openBaiduMapBikeNavi(option, this);

        if (!bikeNavi){
            startWebNavi(start,startAddr,end,endAddr);
        }
    }
    //步行导航
    private void starWalkingNavi(LatLng start, String startAddr, LatLng end, String endAddr) {
        //起点和终点设置
        NaviParaOption option = new NaviParaOption()
                .startName(startAddr)
                .startPoint(start)
                .endName(endAddr)
                .endPoint(end);
        //开启步行导航
        boolean walkNavi = BaiduMapNavigation.openBaiduMapWalkNavi(option, this);
        //未开启
        if (!walkNavi){
            //可以网页导航
            //startWebNavi(start,startAddr,end,endAddr);
            showDialog();
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您未安装百度地图客户端或版本过低，要不要安装？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    // 打开最新版的客户端下载页面
                        OpenClientUtil.getLatestBaiduMapApp(TreasureDetailActivity.this);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }


    private void startWebNavi(LatLng start, String startAddr, LatLng end, String endAddr){
        // 起点和终点的设置
        NaviParaOption option = new NaviParaOption()
                .startName(startAddr)
                .startPoint(start)
                .endName(endAddr)
                .endPoint(end);
        BaiduMapNavigation.openWebBaiduMapNavi(option,this);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    @Override
    public void setDetailData(List<TreasureDetailResult> list) {
        //请求数据的展示
        if (list.size() >= 1){
            TreasureDetailResult result = list.get(0);
            mTvDetailDescription.setText(result.description);
            return;
        }
        mTvDetailDescription.setText("当前宝藏没有详情信息");
    }
}
