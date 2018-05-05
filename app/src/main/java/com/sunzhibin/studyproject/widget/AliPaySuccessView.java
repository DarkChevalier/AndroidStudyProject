package com.sunzhibin.studyproject.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/5/2.
 * description: 仿支付宝支付成功打钩动画
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class AliPaySuccessView extends View {
    private Paint mPaint;
    private Path mHookPath;

    private int mCircleRadius;
    private int mWidth;
    private int mHeight;
    private ValueAnimator mValueAnimator;
    // 起始旋转角度
    private int mBeginAngle;
    private RectF mRectF;
    //钩的绘制进度
    private float mDrawProgress;
    private float mDrawHookProgress;
    //钩长短比例为1:2,故小段的绘制时间占总长的1/3
    private float mFirstHookPercent = 0.33f;
    private float mPaintwidth;

    public AliPaySuccessView(Context context) {
        super(context);
        init();
    }

    public AliPaySuccessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AliPaySuccessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AliPaySuccessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mCircleRadius = (int) dp2px(getContext(), 30);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaintwidth = 7;
        mPaint.setStrokeWidth(mPaintwidth);

        mHookPath = new Path();
        mBeginAngle = 270;
        initAnim();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRectF = new RectF();
        mRectF.left = mWidth / 2 - mCircleRadius;
        mRectF.top = mHeight / 2 - mCircleRadius;
        mRectF.right = mWidth / 2 + mCircleRadius;
        mRectF.bottom = mWidth / 2 + mCircleRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mPaint == null || mRectF == null) {
            return;
        }
        //绘制圆
        canvas.save();
        if (mDrawProgress > 1) {
            //圆已绘制完成
            canvas.rotate(360 + mBeginAngle, mWidth / 2, mHeight / 2);
            canvas.drawArc(mRectF, 0, 360, false, mPaint);
        } else {
            //根据百分比计算扇形的弧度
            int realAngle = (int) (360 * mDrawProgress);
            canvas.rotate(realAngle + mBeginAngle, mWidth / 2, mHeight / 2);
            canvas.drawArc(mRectF, 360 - realAngle, realAngle, false, mPaint);
        }
        canvas.restore();
        if (mDrawProgress > 1) {
            mDrawHookProgress = mDrawProgress - 1;
            //绘制钩
            //圆心 mWidth/2 ，mHeight/2
            //钩的左边起点
            mHookPath.reset();
            float startPointX = (float) (mWidth / 2 - 0.53d * mCircleRadius);
            float startPointY = (float) (mHeight / 2 + 0.05d * mCircleRadius);
            mHookPath.moveTo(startPointX, startPointY);
            //钩的转折点
            float firstHookMoveDistance = (float) (0.35d * mCircleRadius);
            float secondHookMoveDistance = 2 * firstHookMoveDistance;
            if (mDrawHookProgress >= mFirstHookPercent) {
                //开始绘制第二段了
                mHookPath.lineTo(startPointX + firstHookMoveDistance, startPointY + firstHookMoveDistance);
                //转折点到右终点的移动距离
                float secondPercent = (mDrawHookProgress - mFirstHookPercent) / (1 - mFirstHookPercent);
                mHookPath.lineTo(startPointX + firstHookMoveDistance + secondHookMoveDistance * secondPercent
                        , startPointY + firstHookMoveDistance - secondHookMoveDistance * secondPercent);
                //绘制钩第二段
                canvas.drawPath(mHookPath, mPaint);
            } else {
                //还在绘制钩的第一段
                mHookPath.lineTo(startPointX + firstHookMoveDistance * (mDrawHookProgress / mFirstHookPercent), startPointY + firstHookMoveDistance * (mDrawHookProgress / mFirstHookPercent));
                canvas.drawPath(mHookPath, mPaint);
            }
        }


    }

    private void initAnim() {
        mValueAnimator = ValueAnimator.ofFloat(0, 2);
        mValueAnimator.setDuration(1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDrawProgress = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    public void startAnim() {
        mValueAnimator.start();
    }

    public static float dp2px(Context Context, int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Context.getResources().getDisplayMetrics()) + 0.5f;
    }


}
