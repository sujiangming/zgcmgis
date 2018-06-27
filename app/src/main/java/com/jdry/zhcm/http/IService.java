package com.jdry.zhcm.http;

import com.jdry.zhcm.beans.ApiParameter;
import com.jdry.zhcm.beans.CommonBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by JDRY_SJM on 2017/4/17.
 */

public interface IService {
    /**
     * 登录接口
     *
     * @param apiParameter
     * @return
     */
    @POST("api/User/Login")
    Call<CommonBean> login(@Body ApiParameter apiParameter);

    /**
     * 获取断路器信息
     *
     * @param apiParameter
     * @return
     */
    @POST("api/DeviceInfo/GetDeviceInfos")
    Call<CommonBean> getDeviceInfos(@Body ApiParameter apiParameter);

    /**
     * 获取断路器最新状态信息
     *
     * @return
     */
    @POST("api/DeviceSta/GetDevStasNew")
    Call<CommonBean> getDevStas();

    /**
     * 获取告警信息
     *
     * @return
     */
    @POST("api/Alert/GetAlerts")
    Call<CommonBean> getAlerts(@Body ApiParameter apiParameter);

    @POST
    Call<ResponseBody> transferPosition(@Url() String url);


}
