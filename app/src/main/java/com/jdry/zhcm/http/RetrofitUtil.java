package com.jdry.zhcm.http;

import com.jdry.zhcm.BuildConfig;
import com.jdry.zhcm.beans.LoginBean;
import com.jdry.zhcm.global.JDRYDYConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by JDRY_SJM on 2017/4/17.
 */

public class RetrofitUtil {
    public static final int DEFAULT_TIMEOUT = 15;
    public Retrofit mRetrofit;

    private static RetrofitUtil mInstance;

    /**
     * 私有构造方法
     */
    private RetrofitUtil() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(true)//连接失败后是否重新连接
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                //.addHeader("Content-Type", "application/json; charset=UTF-8")
                                //.addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Accept", "*/*")
                                .addHeader("Authorization", LoginBean.getInstance().getToken() == null ? "" : "BasicAuth " + LoginBean.getInstance().getToken())
                                .build();

                        return chain.proceed(request);
                    }
                }).addInterceptor(logging);

        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(JDRYDYConstants.HOST)
                //在此处声明使用FastJsonConverter做为转换器
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                mInstance = new RetrofitUtil();
            }
        }
        return mInstance;
    }

    public <T> T createReq(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }
}
