package com.twq.databindinghelper.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.twq.databindinghelper.R;

/**
 * Created by Administrator on 2018/1/22 0022.
 */

public class RefreshLayout extends ViewGroup {

    private View headView, footerView;
    private int mLayoutContentHeight;
    private TextView tvHead;
    // 用于平滑滑动的Scroller对象
    private Scroller mLayoutScroller;
    private static final float SCROLL_RATIO = 0.3f;// 阻尼系数

    public RefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("NewApi")
    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context) {
        headView = LayoutInflater.from(context).inflate(R.layout.view_header, null, false);
        footerView = LayoutInflater.from(context).inflate(R.layout.view_header, null, false);
        tvHead = headView.findViewById(R.id.tv_head);
        effectiveScrollY = 400;
        mLayoutScroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 看这里哦，亲
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        headView.setLayoutParams(params);
        footerView.setLayoutParams(params);
        addView(headView);
        addView(footerView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLayoutContentHeight = 0;
        // 置位
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == headView) { // 头视图隐藏在顶端
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
            } else if (child == footerView) { // 尾视图隐藏在layout所有内容视图之后
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(), mLayoutContentHeight + child.getMeasuredHeight());
            } else { // 内容视图根据定义(插入)顺序,按由上到下的顺序在垂直方向进行排列
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(), mLayoutContentHeight + child.getMeasuredHeight());
                mLayoutContentHeight += child.getMeasuredHeight();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);

    }

    private int mLastMoveY;
    private int effectiveScrollY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMoveY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = mLastMoveY - y;
                if (dy < 0 && Math.abs(getScrollY()) <= headView.getHeight() / 4) {
                    scrollBy(0, dy);
                    if (Math.abs(getScrollY()) >= effectiveScrollY) {
                        tvHead.setText("松开刷新");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(getScrollY()) >= effectiveScrollY) {
                    mLayoutScroller.startScroll(0, getScrollY(), 0, -(getScrollY() + effectiveScrollY));
                } else {
                    mLayoutScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                }
                break;
        }
        postInvalidate();
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mLayoutScroller.computeScrollOffset()) {
            scrollTo(0, mLayoutScroller.getCurrY());
        }
        postInvalidate();
    }
}
