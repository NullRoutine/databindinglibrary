package com.twq.databindinghelper.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.util.DeviceUtil;
import com.twq.databindinghelper.util.LogUtil;

/**
 * 闪烁文字
 * Created by tang.wangqiang on 2018/5/11.
 */

public class FlickerTextView extends AppCompatTextView {

    private Paint textPaint;
    private LinearGradient mLinearGradient;
    private float textWidth;
    private Matrix textMatrix;
    private float translate = 0.0f;//平移初始值

    private static final int DEFAULT_REFLECTION_COLOR = 0xFFFFFFFF;
    private static final int DEFAULT_DURATION = 1000;
    private int moveColor = DEFAULT_REFLECTION_COLOR;//默认闪烁颜色
    private int duration = DEFAULT_DURATION;//默认动画时间
    private ObjectAnimator animator;


    public FlickerTextView(Context context) {
        this(context, null);
        init(context, null);
    }

    public FlickerTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public FlickerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attributeSet) {
        textPaint = getPaint();
        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.FlickerTextView, 0, 0);
            if (typedArray != null) {
                try {
                    moveColor = typedArray.getColor(R.styleable.FlickerTextView_color_move, DEFAULT_REFLECTION_COLOR);
                    duration = typedArray.getInteger(R.styleable.FlickerTextView_duration_text, DEFAULT_DURATION);
                } catch (Exception e) {
                    LogUtil.e("Error while creating the view:" + e);
                } finally {
                    typedArray.recycle();
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        textWidth = getMeasuredWidth();
        if (textWidth > 0f) {
            mLinearGradient = new LinearGradient(-textWidth, 0, 0,
                    0, new int[]{getCurrentTextColor(), moveColor, getCurrentTextColor()}, new float[]{0, 0.5f, 1
            }, Shader.TileMode.CLAMP);
            textMatrix = new Matrix();
            textPaint.setShader(mLinearGradient);
            if (animator == null) {
                try {
                    controlAnimation();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtil.e("====>onDraw");
        if (textMatrix != null && textPaint.getShader() != null) {
            if (textPaint.getShader() == null) {
                textPaint.setShader(mLinearGradient);
            }
//            translate += textWidth / 4;
//            if (translate > 2 * textWidth) {
//                translate = -textWidth;
//            }
            textMatrix.setTranslate(translate * 2, 0);//矩阵变换核心代码
            mLinearGradient.setLocalMatrix(textMatrix);
//            postInvalidateDelayed(100);
        } else {
            textPaint.setShader(null);
        }
        super.onDraw(canvas);
    }

    public float getTranslate() {
        return translate;
    }

    public void setTranslate(float translate) {
        this.translate = translate;
        invalidate();
    }

    public void controlAnimation() {
        if (textWidth > 0.0f) {
            animator = ObjectAnimator.ofFloat(this, "translate", 0.0f, textWidth);
            animator.setDuration(duration);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        postInvalidate();
                    } else {
                        postInvalidateOnAnimation();
                    }
                    animator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
//            animator.start();
        }
    }


    public void cancel() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 回收资源
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null) {
            cancel();
        }
    }
}
