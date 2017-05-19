package com.yuluedu.maptest.treasure.detail;

import com.yuluedu.maptest.net.NetClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gameben on 2017-05-15.
 */

public class TreasureDetailPresenter {
    private TreasureDetailView mTreasureDetailView;

    public TreasureDetailPresenter(TreasureDetailView treasureDetailView) {
        mTreasureDetailView = treasureDetailView;
    }

    // 获取宝藏详情
    public void getTreasureDetail(TreasureDetail treasureDetail){

        Call<List<TreasureDetailResult>> detailCall = NetClient.getInstance().getTreasureApi().getTreasureDetail(treasureDetail);
        detailCall.enqueue(mCallback);
    }

    private Callback<List<TreasureDetailResult>> mCallback = new Callback<List<TreasureDetailResult>>() {

        // 请求成功
        @Override
        public void onResponse(Call<List<TreasureDetailResult>> call, Response<List<TreasureDetailResult>> response) {
            if (response.isSuccessful()){
                List<TreasureDetailResult> resultList = response.body();
                if (resultList==null){
                    // 弹个吐司说明
                    mTreasureDetailView.showMessage("未知的错误");
                    return;
                }
                // 数据获取到了，将数据设置给视图(TextView展示)
                mTreasureDetailView.setDetailData(resultList);
            }
        }

        // 请求失败
        @Override
        public void onFailure(Call<List<TreasureDetailResult>> call, Throwable t) {
            // 提示信息
            mTreasureDetailView.showMessage("请求失败："+t.getMessage());
        }
    };
}
