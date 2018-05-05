package com.sunzhibin.studyproject.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.sunzhibin.studyproject.R;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/5/3.
 * description: description
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class RippleView extends View {
    //内部圆的半径
    private int ORIGINAL_RADIUS = 0;
    //最大半径
    private int MAX_RADIUS;
    private Paint mPaint;
    //屏幕宽高
    private int mHeight;
    private int mWidth;
    private int mColor;
    private SparseArray<Integer> mListCircle;
    private int mSpaceCircle = 0; //每个同心圆的圆半径差
    private int mSpeedCircle = 1;
    private ValueAnimator mValueAnimator;

    public RippleView(Context context) {
        super(context);
        init();
    }

    public RippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        //关闭GPU硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mColor = getResources().getColor(R.color.colorAccent);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mColor);
        mListCircle = new SparseArray<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mListCircle.size(); i++) {
            drawCircle(canvas, i, mListCircle.get(i));
        }
//        //绘制内部白色圆
//        mPaint.setColor(Color.WHITE);
//        mPaint.setShader(null);
//        canvas.drawCircle(mWidth / 2, mHeight / 2, ORIGINAL_RADIUS, mPaint);

    }

    private void drawCircle(Canvas canvas, int i, int radius) {
        if (radius + mSpeedCircle >= MAX_RADIUS) {
            mListCircle.put(i, ORIGINAL_RADIUS);
            return;
        }
        if (radius + mSpeedCircle > ORIGINAL_RADIUS && radius + mSpeedCircle < MAX_RADIUS) {
            setAlpha(radius + mSpeedCircle);
            canvas.drawCircle(mWidth / 2, mHeight / 2, radius + mSpeedCircle, mPaint);
            mListCircle.put(i, radius + mSpeedCircle);
            if (mListCircle.get(mListCircle.size() - 1) % mSpaceCircle == 0
                    && mListCircle.size() < MAX_RADIUS / mSpaceCircle) {
                mListCircle.append(i + 1, ORIGINAL_RADIUS);
            }
        }

    }

    /**
     * 根据半径设置透明度
     *
     * @param radiu
     */
    private void setAlpha(int radiu) {
        double v = (radiu - ORIGINAL_RADIUS) * 1d / (MAX_RADIUS - ORIGINAL_RADIUS);
        double v1 = (1 - v) * 255;
        if (v1 > 255) {
            v1 = 255;
        }
        if (v1 < 0) {
            v1 = 0;
        }
        mPaint.setAlpha((int) v1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        MAX_RADIUS = mWidth / 2;
        if (mSpaceCircle == 0)
            mSpaceCircle = MAX_RADIUS / 3;
        ORIGINAL_RADIUS = 0;
        initAnim();
    }

    private void initAnim() {
        mValueAnimator = ValueAnimator.ofInt(0, MAX_RADIUS);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.setDuration(MAX_RADIUS * 100);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
//                Log.d("onAnimationUpdate: ", "value: " + value);
                postInvalidate();
            }
        });
//        mValueAnimator.start();
    }

    public void startAnim() {
        initCircle();
        if (mValueAnimator == null) {
            initAnim();
        } else {
            if (mValueAnimator.isRunning()) {
                mValueAnimator.cancel();
            } else
                mValueAnimator.start();
        }
    }

    private void initCircle() {
        mListCircle.clear();
        mListCircle.append(0, ORIGINAL_RADIUS);
//        int index = 0;
//        for (int i = 0; i < MAX_RADIUS; i++) {
//            if (i >= ORIGINAL_RADIUS && i % mSpaceCircle == 0) {
//                mListCircle.append(index, i);
//                index++;
//            }
//        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
    }

    /**
     * 设置同心圆的间隔
     *
     * @param mSpaceCircle
     */
    public void setmSpaceCircle(int mSpaceCircle) {
        this.mSpaceCircle = mSpaceCircle;
    }

    /**
     * 设置同心圆扩散速度
     *
     * @param mSpeedCircle
     */
    public void setmSpeedCircle(int mSpeedCircle) {
        this.mSpeedCircle = mSpeedCircle;
    }

    /**
     * 设置第一个圆大小
     *
     * @param ORIGINAL_RADIUS
     */

    public void setORIGINAL_RADIUS(int ORIGINAL_RADIUS) {
        this.ORIGINAL_RADIUS = ORIGINAL_RADIUS;
    }
}
