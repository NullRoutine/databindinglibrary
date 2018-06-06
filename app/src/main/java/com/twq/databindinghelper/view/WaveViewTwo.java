package com.twq.databindinghelper.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.twq.databindinghelper.util.LogUtil;

/**
 * Created by tang.wangqiang on 2018/5/10.
 */

public class WaveViewTwo extends View {
    private int width = 0;
    private int height = 0;
    private int waveHeight = 100;// 波浪的最高度

    public WaveViewTwo(Context context) {
        this(context, null);
        init();
    }

    public WaveViewTwo(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public WaveViewTwo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
        mShaderMatrix = new Matrix();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        createBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mWaveShader != null) {
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }
            //这里是利用举证的平移变化产生平移效果
            mShaderMatrix.setScale(0.5f, 0.5f, 0, mDefaultWaterLevel);//这里其实是没有变化的
            mShaderMatrix.postTranslate(mWaveShiftRatio * getWidth(), 0);
            mWaveShader.setLocalMatrix(mShaderMatrix);
            canvas.drawRect(0, 0, getWidth(), getHeight(), mViewPaint);
        } else {
            mViewPaint.setShader(null);
        }
        LogUtil.e("=====<onDraw>");
        super.onDraw(canvas);//注意取消super
    }

    private double mDefaultAngularFrequency;
    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private BitmapShader mWaveShader;
    private Paint mViewPaint;
    private float mWaveShiftRatio = 0.5f;
    private Matrix mShaderMatrix;

    public void move() {
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                this, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(2000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        waveShiftAnim.start();
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     * Shift the wave horizontally according to <code>waveShiftRatio</code>.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     *                       Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
            LogUtil.e("===>postInvalidate" + waveShiftRatio);
        }
    }

    private void createBitmap() {
        LogUtil.e("===>" + width + "===>" + height);
        mDefaultAngularFrequency = 2.0f * Math.PI / getWidth();
        mDefaultAmplitude = getHeight() / 20.0f;
        mDefaultWaterLevel = getHeight() * 0.5f;
        mDefaultWaveLength = getWidth();
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setColor(Color.parseColor("#28FFFFFF"));
//        paint.setColor(Color.GRAY);
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, paint);
            waveY[beginX] = beginY;
        }
        paint.setColor(Color.parseColor("#3CFFFFFF"));
//        paint.setColor(Color.BLUE);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, paint);
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }
}
