package com.yuluedu.maptest.treasure.hide;

import com.yuluedu.maptest.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gameben on 2017-05-18.
 */

public class HideTreasurePresenter {

    private HideTreasureView mHideTreasureView;

    public HideTreasurePresenter(HideTreasureView mHideTreasureView) {
        this.mHideTreasureView = mHideTreasureView;
    }

    public void hideTreasure(HideTreasure hideTreasure){
        mHideTreasureView.showProgress();

        Call<HideTreasureResult> resultCall = NetClient.getInstance().getTreasureApi().hideTreasure(hideTreasure);
        resultCall.enqueue(mResultCallback);
    }
    Callback<HideTreasureResult> mResultCallback = new Callback<HideTreasureResult>() {
        @Override
        public void onResponse(Call<HideTreasureResult> call, Response<HideTreasureResult> response) {
            //隐藏进度条
            mHideTreasureView.hideProgress();
            if (response.isSuccessful()){
                HideTreasureResult hideTreasureResult = response.body();
                if (hideTreasureResult == null){

                    mHideTreasureView.showMessage("未知错误");

                    return;
                }
                //真正上传成功
                if (hideTreasureResult.getCode() == 1){
                    //跳回到home页面
                    mHideTreasureView.navigateToHome();
                }
                //提示信息
                mHideTreasureView.showMessage(hideTreasureResult.getMsg());
            }
        }

        @Override
        public void onFailure(Call<HideTreasureResult> call, Throwable t) {
            //隐藏进度条
            mHideTreasureView.hideProgress();
            //提示信息
            mHideTreasureView.showMessage("请求失败："+t.getMessage());
        }
    };
}
