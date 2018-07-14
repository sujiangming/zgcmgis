package com.jdry.zhcm.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.jdry.zhcm.R;

import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by JDRY-SJM on 2017/9/6.
 */

public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, final GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Glide.with(activity)
                .load("file://" + path)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                .skipMemoryCache(true)
                //.centerCrop()
                .into(new ImageViewTarget<GlideDrawable>(imageView) {
                    @Override
                    protected void setResource(GlideDrawable resource) {
                        imageView.setImageDrawable(resource);
                    }

                    @Override
                    public Request getRequest() {
                        return (Request) imageView.getTag(R.id.adapter_item_tag_key);
                    }

                    @Override
                    public void setRequest(Request request) {
                        imageView.setTag(R.id.adapter_item_tag_key, request);
                    }
                });
    }

    @Override
    public void clearMemoryCache() {

    }

    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context)
                .load(path)
                .crossFade()
                .placeholder(R.drawable.zhanweitu)
                .dontAnimate()
                .error(R.drawable.zhanweitu)
                .priority(Priority.NORMAL)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    public void displayCircleRadius(Context context, Object path, ImageView imageView, int radius) {
        Glide.with(context)
                .load(path)
                .crossFade()
                .transform(new GlideCircleTransform(context, radius))
                .placeholder(R.drawable.zhanweitu)
                .dontAnimate()
                .error(R.drawable.zhanweitu)
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

    public void displayByImgRes(Context context, Object path, ImageView imageView, int imgRes) {
        Glide.with(context)
                .load(path)
                .crossFade()
                .placeholder(imgRes)
                .error(imgRes)
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
}
