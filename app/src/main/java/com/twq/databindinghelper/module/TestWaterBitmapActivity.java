package com.twq.databindinghelper.module;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityWaterBitmapBinding;
import com.twq.databindinghelper.util.BitmapCompressUtil;
import com.twq.databindinghelper.util.LogUtil;
import com.twq.databindinghelper.util.WaterBitmapUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 测试水印图片
 * Created by tang.wangqiang on 2018/5/4.
 */

public class TestWaterBitmapActivity extends DataBindingActivity<ActivityWaterBitmapBinding> {
    private static final String TAG = "TAG";

    @Override
    public void create(Bundle savedInstanceState) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_test_water);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap textBitmap = WaterBitmapUtils.drawTextToRightBottom(mContext, bitmap, "右下角", 12, Color.RED, 12, 10);
        textBitmap = WaterBitmapUtils.drawTextToLeftTop(mContext, textBitmap, "左上角", 12, Color.RED, 12, 10);
        textBitmap = WaterBitmapUtils.createWaterMaskRightTop(mContext, textBitmap, bitmap1, 10, 10);
        getBinding().imgTest.setImageBitmap(textBitmap);
        final Bitmap bitmap2 = BitmapCompressUtil.getSampleBitmap2(mContext, R.mipmap.ic_test_bitmap, 450);
        getBinding().imgPhoto.setImageBitmap(bitmap2);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap3 = BitmapCompressUtil.compressImage(bitmap2);
                LogUtil.e(bitmap3.getByteCount() / 1024 + "kb");
            }
        }).start();
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_water_bitmap;
    }
}
