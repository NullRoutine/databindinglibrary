package com.twq.databindinghelper.module;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityWaterBitmapBinding;
import com.twq.databindinghelper.util.WaterBitmapUtils;

/**
 * 测试水印图片
 * Created by tang.wangqiang on 2018/5/4.
 */

public class TestWaterBitmapActivity extends DataBindingActivity<ActivityWaterBitmapBinding> {
    @Override
    public void create(Bundle savedInstanceState) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_test_water);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap textBitmap = WaterBitmapUtils.drawTextToRightBottom(mContext, bitmap, "右下角", 12, Color.RED, 12, 10);
        textBitmap = WaterBitmapUtils.drawTextToLeftTop(mContext, textBitmap, "左上角", 12, Color.RED, 12, 10);
        textBitmap = WaterBitmapUtils.createWaterMaskRightTop(mContext, textBitmap, bitmap1, 10, 10);
        getBinding().imgTest.setImageBitmap(textBitmap);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_water_bitmap;
    }
}
