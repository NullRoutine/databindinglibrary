package com.twq.databindinghelper.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.twq.databindinghelper.R;
import com.twq.databindinghelper.view.GlideCircleTransform;

import java.io.File;

/**
 * 图片加载
 * Created by Administrator on 2017/8/23 0023.
 */

public class LoadImgUtil {

    public static void loadImage(Context context, String url, ImageView img) {
        Glide.with(context).load(url)
                .thumbnail(0.4f)
                .dontAnimate().into(img);
    }

    public static void loadImage(Context context, int url, ImageView img) {
        Glide.with(context).load(url).dontAnimate().into(img);
    }

    public static void loadImage(Context context, Uri url, ImageView img) {
        Glide.with(context).load(url).dontAnimate().into(img);
    }

    /**
     * 加载圆形头像
     *
     * @param context
     * @param url
     * @param img
     */
    public static void loadCircleImage(Context context, Uri url, ImageView img) {
        Glide.with(context).load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontAnimate().placeholder(R.mipmap.ic_launcher_round)
                .transform(new GlideCircleTransform(context))
                .into(img);
    }

    public static void loadCircleImageByUrl(Context context, String url, ImageView img) {
        Glide.with(context).load(url).dontAnimate().placeholder(R.mipmap.ic_launcher_round)
                .transform(new GlideCircleTransform(context))
                .into(img);
    }

    public class CustomGlideUrl extends GlideUrl {

        private String mUrl;

        public CustomGlideUrl(String url) {
            super(url);
            mUrl = url;
        }

        @Override
        public String getCacheKey() {
            return mUrl.replace(findTokenParam(), "");
        }

        private String findTokenParam() {
            String tokenParam = "";
            return tokenParam;
        }

    }
}
