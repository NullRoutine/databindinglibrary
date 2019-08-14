package com.twq.databindinghelper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.twq.databindinghelper.util.DeviceUtil;

/**
 * Created by tang.wangqiang
 * on 2019/8/13
 */
public class SignView extends View {

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    Canvas signCanvas;

    Bitmap mBitmap;

    Path mPath = new Path();

    private float cur_x, cur_y;

    public SignView(Context context) {
        this(context, null);
    }

    public SignView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
//        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DeviceUtil.dip2px(context, 4));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        signCanvas = new Canvas(mBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(event.getX(), event.getY());
                cur_x = x;
                cur_y = y;
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(cur_x, cur_y, x, y);
                cur_x = x;
                cur_y = y;
                break;
            case MotionEvent.ACTION_UP:
                signCanvas.drawPath(mPath, mPaint);
                mPath.reset();
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint);
    }
}
