package com.jdry.zhcm.http;

import com.jdry.zhcm.beans.CommonBean;
import com.jdry.zhcm.beans.DeviceBean;
import com.jdry.zhcm.beans.SaltBean;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by JDRY_SJM on 2017/4/17.
 */

public interface IService {
    /**
     * 获取盐值
     *
     * @return
     */
    @FormUrlEncoded
    @POST("app/getSalt")
    Call<SaltBean> getSalt(@Field("data") String jsonParameter);

    @FormUrlEncoded
    @POST("app/login")
    Call<CommonBean> login(@Field("data") String jsonParameter);

    /**
     * 修改密码
     * @param jsonParameter
     * @return
     */
    @FormUrlEncoded
    @POST("app/api/updatePassword")
    Call<CommonBean> updatePassword(@Field("data") String jsonParameter);

    /**
     * 确认修改密码
     * @param jsonParameter
     * @return
     */
    @FormUrlEncoded
    @POST("app/api/confirmUpdatePassword")
    Call<CommonBean> confirmUpdatePassword(@Field("data") String jsonParameter);

    /**
     * 查询所有设备
     * @param jsonParameter
     * @return
     */
    @FormUrlEncoded
    @POST("app/api/device/getDeviceAllList")
    Call<DeviceBean> getDeviceAllList(@Field("data") String jsonParameter);

    /**
     * 保存巡检记录
     * @param jsonParameter
     * @return
     */
    @FormUrlEncoded
    @POST("app/api/inspection/saveDeviceInspection")
    Call<CommonBean> saveDeviceInspection(@Field("data") String jsonParameter);

    /**
     * 上传图片
     *
     * @param multipartBody
     * @return
     */
    @POST
    Call<ResponseBody> upload(@Url() String url, @Body MultipartBody multipartBody);


    /**
     * 更新设备
     *
     * @param jsonParameter
     * @return
     */
    @FormUrlEncoded
    @POST(" app/api/device/updateDeviceLocation")
    Call<CommonBean> updateDeviceLocation(@Field("data") String jsonParameter);


}
