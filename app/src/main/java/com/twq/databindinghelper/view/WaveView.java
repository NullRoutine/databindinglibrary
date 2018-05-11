package com.twq.databindinghelper.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by tang.wangqiang on 2018/5/10.
 */

public class WaveView extends View {
    private int width = 0;
    private int height = 0;
    private int baseLine = 0;// 基线，用于控制水位上涨的，这里是写死了没动，你可以不断的设置改变。
    private Paint mPaintLeft, mPaintRight;
    private int waveHeight = 100;// 波浪的最高度
    private int waveWidth;//波长
    private float offset = 0f;//偏移量

    private Path leftPath, rightPath;

    public WaveView(Context context) {
        this(context, null);
        init(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaintLeft = new Paint();
        mPaintLeft.setAntiAlias(true);
        mPaintLeft.setColor(Color.RED);
        mPaintLeft.setStyle(Paint.Style.FILL);

        mPaintRight = new Paint();
        mPaintRight.setAntiAlias(true);
        mPaintRight.setColor(Color.BLUE);
        mPaintRight.setStyle(Paint.Style.FILL);

        leftPath = new Path();
        rightPath = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        waveWidth = width;
        baseLine = height / 2;
        updateXControl();
        updateXControlTwo();
    }

    /**
     * 不断的更新偏移量，并且循环。
     */
    private void updateXControl() {
        //设置一个波长的偏移
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0, waveWidth);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (float) animation.getAnimatedValue();//不断的设置偏移量，并重画
                postInvalidate();
            }
        });
        mAnimator.setDuration(5000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }

    /**
     * 不断的更新偏移量，并且循环。
     */
    private void updateXControlTwo() {
        //设置一个波长的偏移
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0, waveWidth);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (float) animation.getAnimatedValue();//不断的设置偏移量，并重画
                postInvalidate();
            }
        });
        mAnimator.setDuration(4000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(getPathLeft(), mPaintLeft);
//        canvas.drawPath(getPathRight(), mPaintRight);
    }

    private Path getPathLeft() {
        leftPath.reset();
        int itemWidth = waveWidth / 2;
        leftPath.moveTo(-itemWidth * 3, waveHeight);
        for (int i = -3; i < 2; i++) {
            int startX = i * itemWidth;
            leftPath.quadTo(
                    startX + itemWidth / 2 + offset,//控制点的X,（起始点X + itemWidth/2 + offset)
                    getWaveHeigh(i),//控制点的Y
                    startX + itemWidth + offset,//结束点的X
                    baseLine);//结束点的Y
        }
        //下面这三句话很重要，它是形成了一封闭区间，让曲线以下的面积填充一种颜色，大家可以把这3句话注释了看看效果。
        leftPath.lineTo(width, height);
        leftPath.lineTo(0, height);
        leftPath.close();
        return leftPath;
    }

    private Path getPathRight() {
        rightPath.reset();
        int itemWidth = waveWidth / 2;
        rightPath.moveTo(-itemWidth * 3, waveHeight);
        for (int i = -3; i < 2; i++) {
            int startX = i * itemWidth;
            rightPath.quadTo(
                    startX + itemWidth / 2 + offset,//控制点的X,（起始点X + itemWidth/2 + offset)
                    getWaveHeigh(i + 1),//控制点的Y
                    startX + itemWidth + offset,//结束点的X
                    baseLine);//结束点的Y
        }
        //下面这三句话很重要，它是形成了一封闭区间，让曲线以下的面积填充一种颜色，大家可以把这3句话注释了看看效果。
        rightPath.lineTo(width, height);
        rightPath.lineTo(0, height);
        rightPath.close();
        return rightPath;
    }

    //奇数峰值是正的，偶数峰值是负数
    private int getWaveHeigh(int num) {
        if (num % 2 == 0) {
            return baseLine + waveHeight;
        }
        return baseLine - waveHeight;
    }
}
