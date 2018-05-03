package com.twq.databindinghelper.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import com.twq.databindinghelper.R;

import java.util.TimeZone;

/**
 * Created by tang.wangqiang on 2018/5/3.
 */

public class ClockView extends View {
    private Time mCalendar; //用来记录当前时间
    // 用来存放三张图片资源
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mDial;
    /**
     * 用来记录表盘图片的宽和高，
     * 以便帮助我们在onMeasure中确定View的大
     * 小，毕竟，我们的View中最大的一个Drawable就是它了。
     */
    private int mDialWidth;
    private int mDialHeight;
    /**
     * 用来记录View是否被加入到了Window中，我们在View attached到
     * Window时注册监听器，监听时间的变更，并根据时间的变更，改变自己
     * 的绘制，在View从Window中剥离时，解除注册，因为我们不需要再监听
     * 时间变更了，没人能看得到我们的View了。
     */
    private boolean mAttached;
    //看名字
    private float mMinutes;
    private float mHour;
    //用来跟踪我们的View 的尺寸的变化，
    // 当发生尺寸变化时，我们在绘制自己
    // 时要进行适当的缩放。
    private boolean mChanged;

    public ClockView(Context context) {
        this(context, null);
        init(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Resources resources = context.getResources();
        if (mDial == null) {
            mDial = resources.getDrawable(R.mipmap.clock_dial);
        }
        if (mHourHand == null) {
            mHourHand = resources.getDrawable(R.mipmap.clock_hand_hour);
        }
        if (mMinuteHand == null) {
            mMinuteHand = resources.getDrawable(R.mipmap.clock_hand_minute);
        }
        mDialHeight = mDial.getIntrinsicHeight();
        mDialWidth = mDial.getIntrinsicWidth();
        mCalendar = new Time();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        float hScale = 1.0f;
        float vScale = 1.0f;
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }
        float scale = Math.min(hScale, vScale);
        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0), resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));

    }

    private void onTimeChanged() {
        mCalendar.setToNow();
        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
        /*这里我们为什么不直接把minute设置给mMinutes，而是要加上 second /60.0f呢，这个值不是应该一直为0吗？
        这里又涉及到Calendar的 一个知识点， 也就是它可以是Linient模式， 此模式下，second和minute是可能超过60和24
        的，具体这里就不展开了， 如果不是很清楚，建议看看Google的官方文档中讲Calendar的部分
       */
        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;
        mChanged = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }
        int availableWidth = super.getRight() - super.getLeft();
        int availableHeight = super.getBottom() - super.getTop();
        int x = availableWidth / 2;
        int y = availableHeight / 2;
        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();
        boolean scaled = false;
        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w, (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }
        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);
        canvas.save();
        canvas.rotate(mHour / 12.0f * 360.0f, x, y);
        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        minuteHand.draw(canvas);
        canvas.restore();
        //最后，我们把缩放的坐标系复原。
        if (scaled) {
            canvas.restore();
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
            onTimeChanged();
            invalidate();
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_TIME_TICK);
            intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
            intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            getContext().registerReceiver(broadcastReceiver, intentFilter);
        }
        mCalendar = new Time();
        onTimeChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(broadcastReceiver);
            mAttached = false;
        }
    }


}
