package com.sunzhibin.studyproject.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/4/22.
 * description: description
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class TouchPullView extends View {
    private final static String TAG = "TouchPullView";
    private Paint mCirclrPaint;
    private int mCircleRadius = 150;
    private float mCenterX = 0;
    private float mCenterY = 0;
    private float mProgress = 0;
    //可拖动的高度
    private int mDragHeight = 800;

    public TouchPullView(Context context) {
        super(context);
        init();
    }

    public TouchPullView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TouchPullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TouchPullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    /**
     * 初始化方法
     */
    private void init() {
        mCirclrPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置抗锯齿
        mCirclrPaint.setAntiAlias(true);
        //设置防抖动
        mCirclrPaint.setDither(true);
        mCirclrPaint.setStyle(Paint.Style.FILL);
        mCirclrPaint.setColor(0xFFFF0000);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mCirclrPaint);


    }

    /**
     * 当进行测量时触发
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int minWidth = 2 * mCircleRadius + getPaddingLeft() + getPaddingRight();
        int minHeight = (int) ((mDragHeight * mProgress + 0.5f) + getPaddingTop() + getPaddingBottom());

        int measureWidth;
        if (widthMode == MeasureSpec.AT_MOST) {
            //最大值
            measureWidth = width;
        } else if (widthMode == MeasureSpec.EXACTLY) {
            //确切值
            measureWidth = Math.min(width, minWidth);
        } else { //MeasureSpec.UNSPECIFIED
            measureWidth = minWidth;
        }
        Log.d(TAG, "onMeasure: "
                + " heightMode: " + heightMode + " MeasureSpec.AT_MOST: " + MeasureSpec.AT_MOST
                + " MeasureSpec.EXACTLY: " + MeasureSpec.EXACTLY
                + " MeasureSpec.UNSPECIFIED: " + MeasureSpec.UNSPECIFIED);
        Log.d(TAG, "onMeasure: "
                + " widthMode: " + widthMode + " MeasureSpec.AT_MOST: " + MeasureSpec.AT_MOST
                + " MeasureSpec.EXACTLY: " + MeasureSpec.EXACTLY
                + " MeasureSpec.UNSPECIFIED: " + MeasureSpec.UNSPECIFIED);
        int measureHeight;
        if (heightMode == MeasureSpec.AT_MOST) {
            //最大值
            measureHeight = height;
        } else if (heightMode == MeasureSpec.EXACTLY) {
            //确切值
            measureHeight = Math.min(height, minHeight);
        } else { //MeasureSpec.UNSPECIFIED
            measureHeight = minHeight;
        }
        //设置宽高
        Log.d(TAG, "onMeasure: "
                + " minWidth: " + minWidth + " minHeight: " + minHeight
                + " measureWidth: " + measureWidth
                + " measureHeight: " + measureHeight);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged:" + " w: " + w + " h: " + h + " oldw: " + oldw + " oldh: " + oldh);
        mCenterX = getWidth() >> 1;
        mCenterY = getHeight() >> 1;
        mCircleRadius = (getWidth() - getPaddingLeft() - getPaddingRight()) >> 1;
    }

    /**
     * 设置进度
     *
     * @param mProgress
     */
    public void setmProgress(float mProgress) {
        this.mProgress = mProgress;
        Log.d(TAG, "进度: " + mProgress);
        requestLayout();
    }
}

