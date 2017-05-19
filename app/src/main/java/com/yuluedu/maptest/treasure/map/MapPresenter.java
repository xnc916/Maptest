package com.yuluedu.maptest.treasure.map;

import com.yuluedu.maptest.net.NetClient;
import com.yuluedu.maptest.treasure.Area;
import com.yuluedu.maptest.treasure.Treasure;
import com.yuluedu.maptest.treasure.TreasureRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gameben on 2017-05-05.
 */

public class MapPresenter {
    private MapMvpView mMapMvpView;
    private Area mArea;

    public MapPresenter(MapMvpView mapMvpView) {
        mMapMvpView = mapMvpView;
    }

    // 获取宝藏数据
    public void getTreasure(Area area){

        // 当前区域已经缓存过，就不再去请求
        if (TreasureRepo.getInstance().isCached(area)){
            return;
        }
        mArea = area;
        Call<List<Treasure>> listCall = NetClient.getInstance().getTreasureApi().getTreasureInArea(area);
        listCall.enqueue(mListCallback);
    }

    private Callback<List<Treasure>> mListCallback = new Callback<List<Treasure>>() {
        // 请求成功
        @Override
        public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
            // 成功
            if (response.isSuccessful()){
                // 拿到响应体数据
                List<Treasure> treasureList = response.body();

                if (treasureList==null){
                    // 弹个吐司说明一下
                    mMapMvpView.showMessage("未知的错误");
                    return;
                }

                //LogUtils.i("shuju"+treasureList.size());
                // 做一个缓存:缓存请求的数据和区域
                TreasureRepo.getInstance().addTreasure(treasureList);
                TreasureRepo.getInstance().cache(mArea);

                // 拿到数据：给MapFragment设置上，在地图上展示
                mMapMvpView.setTreasureData(treasureList);
            }
        }

        // 请求失败
        @Override
        public void onFailure(Call<List<Treasure>> call, Throwable t) {
            // 弹个吐司
            mMapMvpView.showMessage("请求失败："+t.getMessage());
        }
    };
}
