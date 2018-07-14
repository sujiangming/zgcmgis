package com.jdry.zhcm.http;


import com.jdry.zhcm.BuildConfig;
import com.jdry.zhcm.global.JDRYDYConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by JDRY_SJM on 2017/4/17.
 */

public class RetrofitUtil {
    public static final int DEFAULT_TIMEOUT = 30;

    public Retrofit mRetrofit;

    public RetrofitUtil() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        //可以利用okhttp实现缓存
        OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//连接失败后是否重新连接
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        //创建retrofit对象
        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(JDRYDYConstants.HOST)
                .addConverterFactory(FastJsonConverterFactory.create())//在此处声明使用FastJsonConverter做为转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public <T> T createReq(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }
}
