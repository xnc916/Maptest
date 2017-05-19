package com.yuluedu.maptest.net;

/**
 * Created by gameben on 2017-05-09.
 */

import com.yuluedu.maptest.treasure.Area;
import com.yuluedu.maptest.treasure.Treasure;
import com.yuluedu.maptest.treasure.detail.TreasureDetail;
import com.yuluedu.maptest.treasure.detail.TreasureDetailResult;
import com.yuluedu.maptest.treasure.hide.HideTreasure;
import com.yuluedu.maptest.treasure.hide.HideTreasureResult;
import com.yuluedu.maptest.user.User;
import com.yuluedu.maptest.user.account.Update;
import com.yuluedu.maptest.user.account.UpdateResult;
import com.yuluedu.maptest.user.account.UploadResult;
import com.yuluedu.maptest.user.login.LoginResult;
import com.yuluedu.maptest.user.register.RegisterResult;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by gameben on 2017-04-06.
 */


// 对应服务器的接口
public interface TreasureApi {


    //构建get请求
    @GET("http://www.baidu.com")
    @Headers({"content-Length:1024"})
    Call<ResponseBody> getData();
    /**
     * 注解：
     * 1.请求的方式 @GET @POST @PUT。。。
     * 2.请求的URL处理*
     *      1.可替换块 {id} 替换的内容 @Path注解来替换
     *      @GET("group/{id}/users")
    Call<ResponseBody> groupList(@Path("id") int groupId);
     *
     *      2.查询参数：@Query("查询的参数的KEY")
     *                  参数比较多：@QueryMap
     *
     *      3.请求头信息的处理：添加：@Headers({})
     *                       修改：@Header("x-type")String header 参数动态修改的
     *
     *
     *
     *      @GET("group/{id}/users?sort = desc")
     @Headers({"x-type:123","x-length:1024"})
     Call<ResponseBody> groupList(@Path("id") int groupId,
     @Query("sort") String sort ,
     @QueryMap Map<String,String> map,
     @Header("x-type")String header,
     @Body String json);
      *
      *      4.请求体：@Body String json；
      *              1.表单：@FormUrlEncoded
      *                      @Field("password")String password
     *                      @FieldMap Map<String,String> map 键值对多时用map
     *                          @POST("ccc")
     @FormUrlEncoded//表单
     Call<ResponseBody> getFormUrl(
     @Field("username")String username,
     @Field("password")String password,
     @FieldMap Map<String,String> map
     );
      *
      *              2.多部分提交
      *                      @Multipart
     *                      @Part("name")String name
     *                      @PartMap Map<String,String> map
     *                          @POST("vvv")
     @Multipart//多部分提交
     Call<ResponseBody> getMultUrl(@Part("photo")File file,
     @Part("name")String name,
     @PartMap Map<String,String> map);
     */



//    @POST("/Handler/UserHandler.ashx?action=login")
//    Call<ResponseBody> login(@Body RequestBody requestBody);

    //登陆的请求
    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User user);

    //注册的请求
    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User user);

    // 获取区域内的宝藏数据
    @POST("/Handler/TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasureInArea(@Body Area area);

    // 宝藏详情的数据获取
    @POST("/Handler/TreasureHandler.ashx?action=tdetails")
    Call<List<TreasureDetailResult>> getTreasureDetail(@Body TreasureDetail treasureDetail);

    // 埋藏宝藏的请求
    @POST("/Handler/TreasureHandler.ashx?action=hide")
    Call<HideTreasureResult> hideTreasure(@Body HideTreasure hideTreasure);

    // 头像的上传
    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UploadResult> upload(@Part MultipartBody.Part part);

    // 用户头像的更新
    @POST("/Handler/UserHandler.ashx?action=update")
    Call<UpdateResult> update(@Body Update update);
}

