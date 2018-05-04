package com.twq.databindinghelper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.util.LogUtil;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 模拟时钟
 * Created by tang.wangqiang on 2018/5/3.
 */

public class TimeClockView extends View {

    private Paint paintHour;//时线
    private Paint paintMinute;//分线
    private Paint paintSec;//秒线
    private Paint paintLine;//刻度线
    private Paint paintText;//表盘文字
    private Paint paintCircle;//圆心

    private int width, height;//时钟宽高
    private Rect textBound;//创建一个矩形
    private Calendar mCalendar;
    private boolean mAttach = false;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            mCalendar = Calendar.getInstance();
            postInvalidate();
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mCalendar = Calendar.getInstance();
                    handler.sendEmptyMessageDelayed(1, 1000);
                    invalidate();
                    break;
                default:

                    break;
            }
            return false;
        }
    });

    public TimeClockView(Context context) {
        this(context, null);
    }

    public TimeClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paintText = new Paint();
        paintText.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.FILL_AND_STROKE);

        paintLine = new Paint();
        paintLine.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        paintLine.setAntiAlias(true);
        paintLine.setStrokeWidth(2);
        paintLine.setStyle(Paint.Style.FILL);

        paintHour = new Paint();
        paintHour.setAntiAlias(true);
        paintHour.setStyle(Paint.Style.FILL);
        paintHour.setStrokeWidth(10);
        paintHour.setColor(Color.BLACK);

        paintMinute = new Paint();
        paintMinute.setAntiAlias(true);
        paintMinute.setStyle(Paint.Style.FILL);
        paintMinute.setStrokeWidth(6);
        paintMinute.setColor(Color.RED);

        paintSec = new Paint();
        paintSec.setAntiAlias(true);
        paintSec.setStyle(Paint.Style.FILL);
        paintSec.setStrokeWidth(4);
        paintSec.setColor(Color.GREEN);


        textBound = new Rect();
//        mTimer.schedule(mTimerTask, 0, 1000);
//        handler.sendEmptyMessage(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        if (hMode != MeasureSpec.UNSPECIFIED) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        if (wMode != MeasureSpec.UNSPECIFIED) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        setMeasuredDimension(width, height);//定下时钟View宽高
        LogUtil.e("onMeasure");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtil.e("onDraw");
        int circleRadius;
        if (width > height) {
            circleRadius = height / 2 - 130;
        } else {
            circleRadius = width / 2 - 130;
        }
        for (int i = 1; i <= 60; i++) {
            canvas.save();//保存当前的画布
            if (i % 5 == 0) {
                canvas.rotate(360 / 60 * i, width / 2, height / 2);
                paintText.setTextSize(circleRadius / 17);
                //如果绘制对应的数字时只进行一次旋转是不能达到目标的，需要再次以书写文字的地方在进行反向旋转这样写出来的就是正向的
                canvas.rotate(-360 / 60 * i, width / 2, height / 2 - circleRadius + 5);
                paintText.getTextBounds(String.valueOf(i / 5), 0, String.valueOf(i / 5).length(), textBound);
                canvas.drawText(String.valueOf(i / 5), width / 2 - textBound.width() / 2, height / 2 - circleRadius + textBound.height() / 2 - getBaseline(), paintText);
            } else {
                canvas.rotate(360 / 60 * i, width / 2, height / 2);
                canvas.drawCircle(width / 2, height / 2 - circleRadius, 7, paintLine);
            }
            canvas.restore();
        }

        int minute = mCalendar.get(Calendar.MINUTE);//得到当前分钟数
        int hour = mCalendar.get(Calendar.HOUR);//得到当前小时数
        int sec = mCalendar.get(Calendar.SECOND);//得到当前秒数
        float minuteDegree = minute / 60.0f * 360;
        canvas.save();
        canvas.rotate(minuteDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius + 150, width / 2, height / 2 + circleRadius / 6, paintMinute);
        canvas.restore();

        float hourDegree = (hour * 60 + minute) / 12f / 60 * 360;//得到时钟旋转的角度
        canvas.save();
        canvas.rotate(hourDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius + 300, width / 2, height / 2 + circleRadius / 9, paintHour);
        canvas.restore();

        float secDegree = sec / 60.0f * 360;
        canvas.save();
        canvas.rotate(secDegree, width / 2, height / 2);
        canvas.drawLine(width / 2, height / 2 - circleRadius + 50, width / 2, height / 2 + circleRadius / 6, paintSec);
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtil.e("onAttachedToWindow");
        if (!mAttach) {
            mAttach = true;
            mTimer.schedule(mTimerTask, 0, 1000);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtil.e("onDetachedFromWindow");
        if (mAttach) {
            mAttach = false;
            mTimer.cancel();
            mTimerTask.cancel();
        }
    }
}
