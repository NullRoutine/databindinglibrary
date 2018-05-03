package com.twq.databindinghelper.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.twq.databindinghelper.R;
import com.twq.databindinghelper.util.DeviceUtil;

/**
 * 可以直接绘制文字下划线的TextView
 * Created by tang.wangqiang on 2018/4/26.
 */

public class AutoTextView extends AppCompatTextView {

    private int textLeftColor, textRightColor, lineColor;
    private String textLeft, textRight;
    private Paint paint, linePaint;
    private float textLeftSize, textRightSize;
    private float textLeftPadding, textRightPadding, linePadding;
    private float lineHeight;
    private boolean lineIsNeed;

    public AutoTextView(Context context) {
        super(context);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttr(context, attrs);
        init(context);
    }

    public AutoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttr(context, attrs);
        init(context);
    }

    private void readAttr(Context context, @Nullable AttributeSet attr) {
        TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.AutoTextView);
        if (typedArray != null) {
            //Left
            textLeftColor = typedArray.getColor(R.styleable.AutoTextView_text_left_color, context.getResources().getColor(R.color.colorAccent));
            textLeft = typedArray.getString(R.styleable.AutoTextView_text_left);
            textLeftSize = typedArray.getDimension(R.styleable.AutoTextView_text_left_size, DeviceUtil.sp2px(context, 14));
            textLeftPadding = typedArray.getDimension(R.styleable.AutoTextView_text_left_padding, DeviceUtil.sp2px(context, 1));
            //Right
            textRightColor = typedArray.getColor(R.styleable.AutoTextView_text_right_color, context.getResources().getColor(R.color.colorAccent));
            textRight = typedArray.getString(R.styleable.AutoTextView_text_right);
            textRightSize = typedArray.getDimension(R.styleable.AutoTextView_text_right_size, DeviceUtil.sp2px(context, 14));
            textRightPadding = typedArray.getDimension(R.styleable.AutoTextView_text_right_padding, DeviceUtil.sp2px(context, 1));
            //Line
            lineColor = typedArray.getColor(R.styleable.AutoTextView_line_color, context.getResources().getColor(R.color.colorAccent));
            linePadding = typedArray.getDimension(R.styleable.AutoTextView_line_padding, DeviceUtil.sp2px(context, 0));
            lineHeight = typedArray.getDimension(R.styleable.AutoTextView_line_height, DeviceUtil.sp2px(context, 1));
            lineIsNeed = typedArray.getBoolean(R.styleable.AutoTextView_line_isNeed, false);
            typedArray.recycle();
        }
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);
        if (heightModel == MeasureSpec.EXACTLY) {
            if (lineIsNeed) {
                height = (int) (MeasureSpec.getSize(heightMeasureSpec) + lineHeight);
            }
        } else {
            @SuppressLint("DrawAllocation") Paint paint = new Paint();
            paint.setTextSize(this.getTextSize());
            Paint.FontMetrics fm = paint.getFontMetrics();
            double textHeight = Math.ceil(fm.descent - fm.top) + 2;
            height = (int) (textHeight + getPaddingTop() + getPaddingBottom());//计算文本高度
            if (lineIsNeed) {
                height = (int) (height + lineHeight);
            }
        }
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        if (!TextUtils.isEmpty(textLeft)) {
            paint.setTextSize(textLeftSize);
            paint.setColor(textLeftColor);
            canvas.drawText(textLeft, textLeftPadding, getBaseline(), paint);
        }
        if (!TextUtils.isEmpty(textRight)) {
            paint.setTextSize(textRightSize);
            paint.setColor(textRightColor);
            canvas.drawText(textRight, width - paint.measureText(textRight) - textRightPadding, getBaseline(), paint);
        }
        if (lineIsNeed) {
            linePaint.setColor(lineColor);
            linePaint.setStrokeWidth(lineHeight);
            canvas.drawLine(linePadding, getMeasuredHeight() - lineHeight / 2, getMeasuredWidth() - linePadding, getMeasuredHeight() - lineHeight / 2, linePaint);//线从定义的中间点往两边延伸
        }
    }
}
