package com.yuluedu.maptest.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.yuluedu.maptest.R;
import com.yuluedu.maptest.treasure.Treasure;
import com.yuluedu.maptest.treasure.map.MapFragment;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gameben on 2017-05-10.
 */

public class TreasureView extends RelativeLayout {

    @BindView(R.id.tv_treasureTitle)
    TextView tvTreasureTitle;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_treasureLocation)
    TextView tvTreasureLocation;
    // 代码
    public TreasureView(Context context) {
        super(context);
        init();
    }
    // 布局和代码中使用
    public TreasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    // 布局代码使用，设置style样式
    public TreasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 视图的填充
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_treasure, this, true);
        ButterKnife.bind(this);

    }
    // 可以对外提供一个方法：根据宝藏信息，填充布局中的数据
    public void bindTreasure(@NonNull Treasure treasure){
        // 标题和地址
        tvTreasureTitle.setText(treasure.getTitle());
        tvTreasureLocation.setText(treasure.getLocation());

        // 距离：宝藏距离我们有多远
        double distance = 0.00d; //距离

        //宝藏位置
        LatLng latLng = new LatLng(treasure.getLatitude(),treasure.getLongitude());

        // 定位的位置
        LatLng myLocation = MapFragment.getMyLocation();

        if (myLocation == null){
            distance = 0.00d;
        }

        // 利用百度地图提供的计算工具
        distance = DistanceUtil.getDistance(latLng,myLocation);

        // 规范下一显示的样式
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String text = decimalFormat.format(distance/1000)+"km";
        tvDistance.setText(text);

    }

}
