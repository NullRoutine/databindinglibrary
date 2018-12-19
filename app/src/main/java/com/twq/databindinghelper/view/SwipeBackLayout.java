package com.twq.databindinghelper.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by tang.wangqiang on 2018/8/24.
 */

public class SwipeBackLayout extends FrameLayout {
    private ViewDragHelper mViewDragHelper;
    private Paint mPaint;//画笔
    //当前Activity的DecorView
    private ViewGroup mDecorView;
    //DecorView下的LinearLayout
    private View mRootView;
    // Activity
    private Activity mActivity;
    //触发退出当前Activity的宽度
    private float mSlideWidth;
    //屏幕的宽和高
    private int mScreenWidth;
    private int mScreenHeight;
    //用于记录当前滑动距离
    private int curSlideX;

    public SwipeBackLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mActivity = (Activity) context;
        //初始化画笔
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return false;
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
//                super.onViewReleased(releasedChild, xvel, yvel);
                int left = releasedChild.getLeft();
                if (left <= mSlideWidth) {
                    mViewDragHelper.settleCapturedViewAt(0, 0);
                } else {
                    mViewDragHelper.settleCapturedViewAt(mScreenWidth, 0);
                }
                invalidate();
            }

            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
//                super.onViewPositionChanged(changedView, left, top, dx, dy);
                curSlideX = left;
                invalidate();
                if (changedView == mRootView && left >= mScreenWidth) {
                    mActivity.finish();
                    mActivity.overridePendingTransition(0, 0);
                }
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
//                return super.clampViewPositionHorizontal(child, left, dx);
                left = left >= 0 ? left : 0;
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                return super.clampViewPositionVertical(child, top, dy);
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
//                super.onEdgeDragStarted(edgeFlags, pointerId);
                mViewDragHelper.captureChildView(mRootView, pointerId);
            }
        });
        // 设置从左边缘捕获view
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    public void bindActivity() {
        mDecorView = (ViewGroup) mActivity.getWindow().getDecorView();
        mRootView = mDecorView.getChildAt(0);
        mDecorView.removeView(mRootView);
        this.addView(mRootView);
        mDecorView.addView(this);

        //计算屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        mSlideWidth = dm.widthPixels * 0.28f;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawShadow(canvas);
        super.dispatchDraw(canvas);
    }

    @Override
    public void computeScroll() {
        //使用settleCapturedViewAt方法是，必须重写computeScroll方法，传入true
        //持续滚动期间，不断刷新ViewGroup
        if (mViewDragHelper.continueSettling(true))
            invalidate();
    }

    private void drawShadow(Canvas canvas) {
        canvas.save();
        //构造一个渐变
        Shader mShader = new LinearGradient(curSlideX - 40, 0, curSlideX, 0, new int[]{Color.parseColor("#00dddddd"), Color.parseColor("#33666666"), Color.parseColor("#90666666")}, null, Shader.TileMode.REPEAT);
        //设置着色器
        mPaint.setShader(mShader);
        //绘制时，注意向左边偏移
        RectF rectF = new RectF(curSlideX - 40, 0, curSlideX, mScreenHeight);
        canvas.drawRect(rectF, mPaint);
        canvas.restore();
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        return mViewDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }
}
