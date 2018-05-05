package com.sunzhibin.studyproject.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sunzhibin.studyproject.R;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/5/3.
 * description: 圆形旋转，渐进色
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class RotateCircleView extends View {
    private int mRadius = 10;
    private RectF mRectF;
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private int mRealAngle;
    private SweepGradient mSweepGtadient;
    private boolean mComplete;
    private int mColor;
    private ValueAnimator mValueAnimator;

    public RotateCircleView(Context context) {
        super(context);
        init();
    }

    public RotateCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RotateCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RotateCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mColor = getResources().getColor(R.color.colorAccent);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

        initAnim();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mRectF = new RectF();
        mRectF.left = 0 + mRadius;
        mRectF.top = 0 + mRadius;
        mRectF.right = mWidth - mRadius;
        mRectF.bottom = mHeight - mRadius;

        int[] ints = new int[]{Color.WHITE, mColor};
        mSweepGtadient = new SweepGradient(mWidth / 2, mHeight / 2, ints, null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null) {
            return;
        }
        int paintAngle = mComplete ? 360 : mRealAngle;
        canvas.rotate(mRealAngle, mWidth / 2, mHeight / 2);
        mPaint.setShader(mSweepGtadient);
        mPaint.setStrokeWidth(mRadius);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(mRectF, 360 - paintAngle, paintAngle, false, mPaint);

        //画被白色盖住的半个头部
        if (paintAngle > 2) {
            mPaint.setShader(null);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setStrokeCap(Paint.Cap.SQUARE);
            mPaint.setStrokeWidth(1);
            RectF oval2 = new RectF(mWidth - mRadius * 3 / 2, mHeight / 2 - mRadius / 2 - 2
                    , mWidth - mRadius / 2, mHeight / 2 + mRadius / 2 - 2);
            canvas.drawArc(oval2, 0, 180, true, mPaint);

        }
    }

    private void initAnim() {
        mValueAnimator = ValueAnimator.ofInt(0, 360);
        mValueAnimator.setDuration(1800);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRealAngle = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {


            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                mComplete = true;
            }
        });
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);

    }

    public void startAnim() {
        if (mValueAnimator == null) {
            return;
        }
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        } else {
            mComplete = false;
            mValueAnimator.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                calculateTouchAngle(event);
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);

    }

    /**
     * 计算touch的位置和对应圆中的角度
     */
    private boolean calculateTouchAngle(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        //判断touch的点是否是在圆环上
        if (Math.sqrt(x) + Math.sqrt(y) < Math.sqrt(mWidth / 2 - mRadius)
                || Math.sqrt(x) + Math.sqrt(y) > Math.sqrt(mWidth / 2 - mRadius)) {
            return false;
        }
        //判断touch的点，在圆上的角度


        return true;
    }
}
