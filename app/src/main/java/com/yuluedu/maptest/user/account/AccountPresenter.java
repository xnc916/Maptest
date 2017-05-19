package com.yuluedu.maptest.user.account;

import com.yuluedu.maptest.net.NetClient;
import com.yuluedu.maptest.user.UserPrefs;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gameben on 2017-05-18.
 */

public class AccountPresenter {

    private AccountView mAccountView;

    public AccountPresenter(AccountView mAccountView) {
        this.mAccountView = mAccountView;
    }

    // 上传及更新头像
    public void uploadPhoto(File file){

        mAccountView.showProgress();


        //进行构建上传的文件的部分
        MultipartBody.Part part = MultipartBody.Part.createFormData("file","photo.png", RequestBody.create(null,file));

        Call<UploadResult> upload = NetClient.getInstance().getTreasureApi().upload(part);
        upload.enqueue(mResultCallback);
    }
    private Callback<UploadResult> mResultCallback =new Callback<UploadResult>() {
        @Override
        public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
            mAccountView.hideProgress();
            if (response.isSuccessful()){
                UploadResult uploadResult = response.body();
                if (uploadResult == null){
                    mAccountView.showMessage("未知的错误");
                    return;
                }

                //显示信息
                mAccountView.showMessage(uploadResult.getMsg());

                if (uploadResult.getCount() != 1){
                    return;
                }
                // 可以拿到头像地址数据
                String photourl = uploadResult.getUrl();

                // 可以存储到用户仓库里面、展示出来
                UserPrefs.getInstance().setPhoto(NetClient.BASE_URL+photourl);

                // 将上传的头像在页面上展示出来
                mAccountView.updatePhoto(NetClient.BASE_URL+photourl);

                //更新数据
                // 需要截取一下
                String substring = photourl.substring(photourl.lastIndexOf("/") + 1, photourl.length());
                Update update = new Update(UserPrefs.getInstance().getTokenid(),substring);
                Call<UpdateResult> updateResultCall = NetClient.getInstance().getTreasureApi().update(update);

                updateResultCall.enqueue(mUpdateCallback);
            }
        }

        @Override
        public void onFailure(Call<UploadResult> call, Throwable t) {
            mAccountView.hideProgress();

            mAccountView.showMessage("上传失败："+t.getMessage());
        }
    };
    //更新回掉
    private Callback<UpdateResult> mUpdateCallback = new Callback<UpdateResult>() {

        @Override
        public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
            mAccountView.hideProgress();
            if (response.isSuccessful()){
                UpdateResult updateResult = response.body();
                if (updateResult == null){
                    mAccountView.showMessage("未知的错误");
                    return;
                }
                mAccountView.showMessage(updateResult.getMsg());
                if (updateResult.getCode() != 1){
                    return;
                }
                // 其实也可以在这里去处理头像的展示等。
            }
        }

        @Override
        public void onFailure(Call<UpdateResult> call, Throwable t) {
            mAccountView.hideProgress();
            mAccountView.showMessage("上传失败："+t.getMessage());
        }
    };
}
