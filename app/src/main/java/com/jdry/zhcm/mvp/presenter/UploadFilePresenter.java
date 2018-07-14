package com.jdry.zhcm.mvp.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jdry.zhcm.beans.ImageBean;
import com.jdry.zhcm.global.JDRYDYConstants;
import com.jdry.zhcm.http.IService;
import com.jdry.zhcm.http.RetrofitUtil;
import com.jdry.zhcm.mvp.view.IView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadFilePresenter {

    private IView mIView;

    public UploadFilePresenter(IView iView) {
        this.mIView = iView;
    }

    /**
     * 这种上传方式支持单张图片和多张图片，同时也支持上传除了图片类的其他文件
     *
     * @param files
     * @return
     */
    public void upload(List<File> files, String folderName) {
        MultipartBody multipartBody = filesToMultipartBody(files);
        String url = JDRYDYConstants.HOST + "app/api/upload/" + folderName;
        // 执行请求
        Call<ResponseBody> call = new RetrofitUtil().createReq(IService.class).upload(url, multipartBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String ret = response.body().string();
                        ImageBean imageBean = JSON.parseObject(ret, ImageBean.class);
                        mIView.uploadSuccess(imageBean.getData().getPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                mIView.uploadFailure(t.getMessage());
            }
        });
    }

    public MultipartBody filesToMultipartBody(List<File> files) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (File file : files) {
            // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("img", file.getName(), requestBody);
        }

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }
}
