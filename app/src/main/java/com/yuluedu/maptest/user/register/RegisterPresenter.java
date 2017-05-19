package com.yuluedu.maptest.user.register;


import com.yuluedu.maptest.net.NetClient;
import com.yuluedu.maptest.user.User;
import com.yuluedu.maptest.user.UserPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by gameben on 2017/3/31.
 */

//注册的业务
public class RegisterPresenter {

    private RegisterView mRegisterView;
    public RegisterPresenter(RegisterView registerView){
        mRegisterView = registerView;
    }


    /**1.到TreasureApi 中注册请求
     * 2.具体实现
     */


    public void register(User user){
        //显示进度条
        mRegisterView.showProgress();

        NetClient.getInstance().getTreasureApi().register(user).enqueue(new Callback<RegisterResult>() {
            //请求成功
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                mRegisterView.hideProgress();//隐藏进度
                //响应成功
                if (response.isSuccessful()){
                    RegisterResult registerResult = response.body();

                    //响应体是否为空
                    if (registerResult == null){
                        mRegisterView.showMessage("未知错误");
                        return;
                    }
                    if (registerResult.getCode() == 1){
                        //真的注册成功
                        //保存tokenid
                        UserPrefs.getInstance().setTokenid(registerResult.getTokenId());
                        mRegisterView.navigateToHome();
                    }
                    mRegisterView.showMessage(registerResult.getMsg());
                }
            }
            //请求失败
            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                mRegisterView.hideProgress();//隐藏进度
                mRegisterView.showMessage("请求失败"+t.getMessage());
            }
        });
    }



//    public void register(){
//       new AsyncTask<Void, Integer, Void>() {
//
//           @Override
//           protected void onPreExecute() {
//               super.onPreExecute();
//                mRegisterView.showProgress();
//           }
//
//           @Override
//           protected Void doInBackground(Void... params) {
//               try {
//                   Thread.sleep(3000);
//               } catch (Exception e) {
//                   e.printStackTrace();
//               }
//               return null;
//           }
//
//           @Override
//           protected void onPostExecute(Void aVoid) {
//               super.onPostExecute(aVoid);
//
//               //拿到数据处理UI
//               mRegisterView.hideProgress();
//               mRegisterView.showMessage("注册成功");
//               mRegisterView.navigateToHome();
//           }
//       }.execute();
//
//
//    }

}
