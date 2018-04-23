package com.twq.databindinghelper.module;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.base.DataBindingActivity;
import com.twq.databindinghelper.databinding.ActivityAnimatorBinding;
import com.twq.databindinghelper.util.DeviceUtil;

/**
 * 动画
 * Created by Administrator on 2018/1/25 0025.
 */

public class AnimatorActivity extends DataBindingActivity<ActivityAnimatorBinding> {

    private TransitionSet transitionSet;
    private boolean isExpand;
    private int ScreenWidth;//屏幕宽度

    public static void launch(Context context) {
        Intent intent = new Intent(context, AnimatorActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void create(Bundle savedInstanceState) {
        getBinding().button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAnim();
            }
        });
        isExpand = false;
        ScreenWidth = DeviceUtil.getScreenWidth(mContext);
        getBinding().layoutToolbar.getBackground().mutate().setAlpha(0);
        getBinding().scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                //改变toolbar的透明度
                changeToolbarAlpha();
                if (getBinding().scrollView.getScrollY() >= getBinding().img.getHeight() - getBinding().layoutToolbar.getHeight() && !isExpand) {
                    exPand();//靠系统提供的Transition实现view宽度改变
                    isExpand = true;
//                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(new ViewWrapper(getBinding().layoutTitle), "width", ScreenWidth);//这是靠动画来改变view宽度
//                    setAnimator(objectAnimator, 1);
                } else if (getBinding().scrollView.getScrollY() <= 0 && isExpand) {
                    reDuce();
                    isExpand = false;
//                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(new ViewWrapper(getBinding().layoutTitle), "width", dip2px(70));
//                    setAnimator(objectAnimator, 0);
                }
            }
        });
    }

    private void changeToolbarAlpha() {
        int scrollY = getBinding().scrollView.getScrollY();
        //快速下拉会引起瞬间scrollY<0
        if (scrollY < 0) {
            getBinding().layoutToolbar.getBackground().mutate().setAlpha(0);
            return;
        }
        //计算当前透明度比率
        float radio = Math.min(1, scrollY / (getBinding().img.getHeight() - getBinding().layoutToolbar.getHeight() * 1f));
        //设置透明度
        getBinding().layoutToolbar.getBackground().mutate().setAlpha((int) (radio * 0xFF));
    }

    private void CreateAnim() {
        // 步骤1：设置属性数值的初始值 & 结束值
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getBinding().button.getLayoutParams().width, 500);
        // 步骤2：设置动画的播放各种属性
        valueAnimator.setDuration(1000);
        // 步骤3：将属性数值手动赋值给对象的属性:此处是将 值 赋给 按钮的宽度
        // 设置更新监听器：即数值每次变化更新都会调用该方法
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getBinding().button.getLayoutParams().width = (int) animation.getAnimatedValue();
                // 每次值变化时，将值手动赋值给对象的属性
                // 即将每次变化后的值 赋 给按钮的宽度，这样就实现了按钮宽度属性的动态变化
                // 步骤4：刷新视图，即重新绘制，从而实现动画效果
                getBinding().button.requestLayout();
            }
        });
        valueAnimator.start();//动画启动
    }

    private void setAnimator(ObjectAnimator objectAnimator, final int status) {
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (status == 0) {
                    getBinding().tvTitle.setText("搜索");
                } else if (status == 1) {
                    getBinding().tvTitle.setText("搜索简书的内容和朋友");
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    private void exPand() {
        getBinding().tvTitle.setText("搜索简书的内容和朋友");
//        getBinding().tvTitle.setText("搜索");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) getBinding().layoutTitle.getLayoutParams();
        LayoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        getBinding().layoutTitle.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(getBinding().layoutTitle);
    }

    private void reDuce() {
        getBinding().tvTitle.setText("搜索");
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams) getBinding().layoutTitle.getLayoutParams();
        LayoutParams.width = dip2px(70);
        LayoutParams.setMargins(dip2px(10), dip2px(10), dip2px(10), dip2px(10));
        getBinding().layoutTitle.setLayoutParams(LayoutParams);
        //开始动画
        beginDelayedTransition(getBinding().layoutTitle);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_animator;
    }

    private void beginDelayedTransition(ViewGroup view) {
        transitionSet = new AutoTransition();
        transitionSet.setDuration(200);
        TransitionManager.beginDelayedTransition(view, transitionSet);
    }

    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }
}

