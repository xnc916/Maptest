package com.yuluedu.maptest.net;

/**
 * Created by gameben on 2017-05-09.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gameben on 2017/3/31.
 */

// 网络的客户端类
public class NetClient {
    public static final String BASE_URL = "http://admin.syfeicuiedu.com";
    public static NetClient mNetClient;
    private final OkHttpClient okHttpClient;
    private Gson mGson;
    private Retrofit mRetrofit;
    private TreasureApi mTreasureApi;
    private NetClient(){

        // 设置GSON的非严格模式setLenient()
        mGson = new GsonBuilder().setLenient().create();


        //日志拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        //需要设置打印级别
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //okHttpClient 单例化
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)//必须加
                .client(okHttpClient)//添加okHttpClient
                .addConverterFactory(GsonConverterFactory.create())//添加转换器
                .build();

        //对请求接口的具体实现
        // mTreasureApi = mRetrofit.create(TreasureApi.class);


    }
    //  synchronized 同步锁 线程安全
    public static synchronized NetClient getInstance(){
        if (mNetClient == null){
            mNetClient = new NetClient();
        }
        return mNetClient;
    }
    //讲TreasureApi怎么对外提供处理：提供一个方法getTreasureApi()
    public TreasureApi getTreasureApi(){
        if (mTreasureApi == null){
            //对请求接口的具体实现
            mTreasureApi = mRetrofit.create(TreasureApi.class);
        }
        return mTreasureApi;
    }

//    public Call getData(){
//        final Request request = new Request.Builder()
//                .get()//方式
//                .url("http://www.baidu.com")
//                .addHeader("content-type","html")
//                .addHeader("content-Length","1024")
//                .build();
//
//        return okHttpClient.newCall(request);
//    }
//
//    //构建post请求
//    public Call login(User user){
//       /**
//        *1.当需要上传的数据是键值对的时候
//        *  如 username = xx passwors == xx
//        *
//        *  json = "{username = xx, passwors == xx}"
//        *  用表单 进行提交
//        *
//        *2.当上传的数据是多个部分时
//        *
//        *
//        */
//
//        /**
//        //多部请求体
//        RequestBody multBody = new MultipartBody.Builder()
//                .addFormDataPart("photo","abc.png",RequestBody.create(null,"abc.png"))
//                .addFormDataPart("name","1234560")
//                .build();
//
//        //表单形式 请求体构建
//        RequestBody formBody = new FormBody.Builder()
//                .add("UserName","123456")
//                .add("Password","123456")
//                .build();
//        */
//        //上传的请求体：字符串、文件、数组等
//
////        RequestBody requestBody = RequestBody.create(null,"{\n" +
////                "\"UserName\":\"qjd\",\n" +
////                "\"Password\":\"654321\"\n" +
////                "}");
//        RequestBody requestBody = RequestBody.create(null,mGson.toJson(user));
//        //构建请求体
//        Request request = new Request.Builder()
//                .post(requestBody)
//                .url(BASE_URL+"/Handler/UserHandler.ashx?action=login")
//                .build();
//
//        return okHttpClient.newCall(request);
//    }

}
