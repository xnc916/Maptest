package com.yuluedu.maptest.user.login;

import android.os.Handler;
import android.os.Looper;

import com.yuluedu.maptest.net.NetClient;
import com.yuluedu.maptest.user.User;
import com.yuluedu.maptest.user.UserPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by gameben on 2017/3/31.
 */
// 登录的业务类：帮View去做业务请求
public class LoginPresenter {
    /**
     * 业务类中间涉及到的视图怎么处理？
     * 1. 创建LoginActivity，不能采用这种方式
     * 2. 接口回调的方式
     * A 接口  里面有一个a()
     * AImpl是A接口的实现类  实现a()
     * 使用：A a = new Aimpl();
     * this.a = a;
     * a.a();
     * <p>
     * 接口创建好了，怎么初始化？
     * Activity实现视图接口
     */

    private LoginView mLoginView;
    private Call mCall;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public LoginPresenter(LoginView loginView) {
        mLoginView = loginView;
    }

    public void login(User user){
        mLoginView.showProgress();
        NetClient.getInstance().getTreasureApi().login(user).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                mLoginView.hideProgress();
                //成功
                if (response.isSuccessful()){
                    LoginResult loginResult = response.body();
                    if (loginResult == null){
                        mLoginView.showMessage("未知错误");
                        return;
                    }
                    if (loginResult.getCode() == 1){
                        //真正成功
                        //保存头像和tokenid（用户令牌）
                        UserPrefs.getInstance().setPhoto(NetClient.BASE_URL+loginResult.getHeadpic());
                        UserPrefs.getInstance().setTokenid(loginResult.getTokenid());
                        mLoginView.navigateToHome();
                    }
                    mLoginView.showMessage(loginResult.getMsg());
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                mLoginView.hideProgress();
                mLoginView.showMessage("请求失败"+t.getMessage());
            }
        });
    }
//    public void login(User user){
//
//        RequestBody body = RequestBody.create(null, new Gson().toJson(user));
//        NetClient.getInstance().getTreasureApi().login(body).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                mLoginView.showMessage("请求成功");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                mLoginView.showMessage("请求失败");
//            }
//        });
//    }



//    public void login(User user) {
//        NetClient.getInstance().getTreasureApi().getData().enqueue(new Callback<ResponseBody>() {
//
//            //请求成功
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//            //可以更新UI
//                mLoginView.showMessage("请求成功"+response.code());
//            }
//            //请求失败
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                //可以更新UI
//                mLoginView.showMessage("请求失败");
//            }
//        });
//    }
//      public void login(User user){
//
//          NetClient.getInstance().getTreasureApi().getData();
//          NetClient.getInstance().login(user).enqueue(new Callback() {
//              @Override
//              public void onFailure(Call call, IOException e) {
//                  mHandler.post(new Runnable() {
//                      @Override
//                      public void run() {
//                        mLoginView.showMessage("请求失败");
//                      }
//                  });
//              }
//
//              @Override
//              public void onResponse(Call call, final Response response) throws IOException {
//                  mHandler.post(new Runnable() {
//                      @Override
//                      public void run() {
//                          if (response.isSuccessful()){
//                              ResponseBody responseBody = response.body();
//                              try {
//                                  String json = responseBody.string();
//
//                                  LoginResult loginResult = new Gson().fromJson(json, LoginResult.class);
//
//                                  //Gson
//                                  //mLoginView.showMessage("请求成功"+response.code());
//                                  if (loginResult == null){
//                                    mLoginView.showMessage("未知错误");
//                                      return;
//                                  }
//                                  if (loginResult.getCode() == 1){
//                                      mLoginView.navigateToHome();
//                                  }
//                                  mLoginView.showMessage(loginResult.getMsg());
//
//
//                              } catch (IOException e) {
//                                  e.printStackTrace();
//                              }
//                          }
//                      }
//                  });
//              }
//          });
//      }

//    //登陆业务2的封装
//    public void login(){
//       // mCall.cancel();//call模型的取消
//        mCall = NetClient.getInstance().getData();
//        mCall.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //更新UI
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()){
//                    Log.e("TAG","响应码"+response.code()+",响应体数据"+response.body().string());
//                }
//            }
//        });
//    }

    //登陆业务2
//    public void login(){
//        mLoginView.showProgress();
//
//        //1.创建一个客户端
//        OkHttpClient okHttpClient = new OkHttpClient();
//
//        //2.构建请求
//        //get方式 get请求不需要请求体
//        final Request request = new Request.Builder()
//                .get()//方式
//                .url("http://www.baidu.com")
//                .addHeader("content-type","html")
//                .addHeader("content-Length","1024")
//                .build();
//        //3.发送请求，同步和异步 这里用异步
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            //onFailure 请求失败触发
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//            //onResponse 请求成功1xx---5xx 都走这个
//            //须再判断是否真的成功
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
////                response.body();//响应体数据
////                response.code();//响应码
////                response.headers();//响应头
//
//                //通过isSuccessful方法进一步判断
//                if (response.isSuccessful()){
//                    Log.e("TAG","响应码"+response.code()+",响应体数据"+response.body().string());
//                }
//            }
//        });
////        //同步
////        try {
////            Response execute = okHttpClient.newCall(request).execute();
////            if (execute != null){
////
////            }
////
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//    }


    // 登录的业务1
//    public void login(){
//        /**
//         * 1. 参数：请求的地址、上传的数据等类型，可以为空Void
//         * 2. 进度：一般是Integer(int的包装类)，可以为空Void
//         * 3. 结果：比如String、可以为空Void
//         */
//
//        new AsyncTask<Void,Integer,Void>(){
//
//            // 请求之前的视图处理：比如进度条的显示
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                // 显示一个进度条
//                mLoginView.showProgress();
//            }
//
//            // 后台线程：耗时的操作
//            @Override
//            protected Void doInBackground(Void... params) {
//                // 模拟：休眠3秒钟
//
//                try {
//                    Thread.sleep(3000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//            // 拿到请求的数据，处理UI：进度条隐藏、跳转页面等
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                // 隐藏进度条
//                mLoginView.hideProgress();
//                mLoginView.showMessage("登陆成功");
//                mLoginView.navigateToHome();
//            }
//        }.execute();
//
//
//
//    }

}
