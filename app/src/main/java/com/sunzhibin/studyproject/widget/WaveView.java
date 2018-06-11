package com.sunzhibin.studyproject.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.sunzhibin.studyproject.R;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/6/9.
 * description: description
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class WaveView extends View {
    /**
     * 参考资料：https://github.com/gelitenight/WaveView
     * |<----------------------->|
     * | wave length - 波长      |__________
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude - 振幅
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|_______
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level - 水位
     * |                        |  |
     * |                        |  |
     * +------------------------+__|_______
     */
    //外圆
    private Paint mCircleGradientPaint;
    //圆形描边
    private Paint mCirclePaint;
    //水滴
    private Paint mBezierWaterPaint;
    //百分比文字
    private Paint mProgressTextPaint;
    //波浪
    private Paint mBezierWavePaint;

    //外圆渐进色
    private int[] mGradientColors = new int[]{Color.parseColor("#ffffff"),
            Color.parseColor("#98d7d4")};
    //圆形描边颜色
    private int mCircleColor = Color.parseColor("#addef8");
    private int mProgressColor = Color.parseColor("#5aa828");
    //波浪颜色
    private int mWave1Color = Color.parseColor("#B3addef8");//70%
    private int mWave2Color = Color.parseColor("#80addef8");//50%
    private int mWave3Color = Color.parseColor("#4Daddef8");//30%
    /**
     * 外圆宽度
     */
    private int mCircleGradientStroke = dip2px(getContext(), 11);
    /**
     * 内圆环宽度
     */
    private int mCircleStroke = dip2px(getContext(), 0.5f);
    /**
     * 字号
     */
    private int mProgressTextSize = dip2px(getContext(), 30);
    //内圆距外圆距离
    private int mInnerCircleDistance = dip2px(getContext(), 4);
    private boolean mNeedPercent = true;
    private SweepGradient mSweepGradient;

    private int mHeight;
    private int mWidth;
    //水滴的大小
    private int mWaterDropHeight = dip2px(getContext(), 45);
    private int mWaterDropWidth = dip2px(getContext(), 45);

    private int mRoteDegress = 0;
    private ValueAnimator mValueAnimator;
    private ValueAnimator mDropAnimator;
    private Rect mSrcRect;
    private Rect mDestRect;
    //水滴距离控件顶部的高度
    private int mTempDropHeight;
    //外圆bitmap
    private Bitmap mCircleBitmap;
    private Bitmap mDropBitmap;
    private PorterDuffXfermode mXfermode;

    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    // if true, the shader will display the wave
    private boolean mShowWave;

    // shader containing repeated waves
    private BitmapShader mWaveShader;
    // shader matrix
    private Matrix mShaderMatrix;
    // paint to draw wave
    private Paint mViewPaint;

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;

    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    public WaveView(Context context) {
        super(context);
        init();

    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        mCircleGradientPaint = new Paint();
        mCirclePaint = new Paint();
        mBezierWaterPaint = new Paint();
        mProgressTextPaint = new Paint();
        mBezierWavePaint = new Paint();


        mCircleGradientPaint.setDither(true);
        mBezierWaterPaint.setDither(true);
        mCirclePaint.setDither(true);
        mProgressTextPaint.setDither(true);
        mBezierWavePaint.setDither(true);

        mCircleGradientPaint.setAntiAlias(true);
        mBezierWaterPaint.setAntiAlias(true);
        mCirclePaint.setAntiAlias(true);
        mProgressTextPaint.setAntiAlias(true);
        mBezierWavePaint.setAntiAlias(true);

        mCircleGradientPaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mBezierWaterPaint.setStyle(Paint.Style.FILL);
        mProgressTextPaint.setStyle(Paint.Style.FILL);
        mBezierWavePaint.setStyle(Paint.Style.FILL);

        mCircleGradientPaint.setStrokeWidth(mCircleGradientStroke);//设置线宽
        mCirclePaint.setStrokeWidth(mCircleStroke);//设置线宽

        mProgressTextPaint.setTextSize(mProgressTextSize);
        mProgressTextPaint.setColor(mProgressColor);
        mBezierWaterPaint.setColor(mProgressColor);
        mCirclePaint.setColor(mCircleColor);

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SCREEN);

        //wave
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
            mHeight = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mHeight = heightSize + getPaddingTop() + getPaddingBottom();
            mWidth = widthSize + getPaddingLeft() + getPaddingRight();
        } else {
            mHeight = heightSize + getPaddingTop() + getPaddingBottom();
            mWidth = widthSize + getPaddingLeft() + getPaddingRight();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
            mWidth = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = heightSize + getPaddingTop() + getPaddingBottom();
            mWidth = widthSize + getPaddingLeft() + getPaddingRight();

        } else {
            mHeight = heightSize + getPaddingTop() + getPaddingBottom();
            mWidth = widthSize + getPaddingLeft() + getPaddingRight();

        }

        mWidth = Math.min(mWidth, mHeight);
        mHeight = Math.min(mWidth, mHeight);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        createShader();
        Log.d("mDropAnimator: ", "onSizeChanged mWidth: " + mWidth + " mHeight: " + mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawableWaterDrop(canvas);
        drawableCircleGradient(canvas);
        drawableInnerCircle(canvas);
        drawWave(canvas);
        drawText(canvas);
    }

    /**
     * 外圆、
     *
     * @param canvas
     */
    private void drawableCircleGradient(Canvas canvas) {
        if (mCircleBitmap == null) {
            mCircleBitmap = Bitmap.createBitmap(mWidth,
                    mHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas1 = new Canvas(mCircleBitmap);
            if (mSweepGradient == null) {
                mSweepGradient = new SweepGradient(mWidth / 2, mHeight / 2, mGradientColors, null);
                mCircleGradientPaint.setShader(mSweepGradient);
            }
            canvas1.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - mCircleGradientStroke / 2, mCircleGradientPaint);
        }
        canvas.save();
        canvas.rotate(-90 + mRoteDegress % 360, mWidth / 2, mHeight / 2);
        canvas.drawBitmap(mCircleBitmap, 0, 0, mCircleGradientPaint);
        canvas.restore();
    }

    /**
     * 内圆、
     *
     * @param canvas
     */
    private void drawableInnerCircle(Canvas canvas) {
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStrokeWidth(mCircleStroke);
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, mWidth / 2f - mCircleGradientStroke - mInnerCircleDistance, mCirclePaint);

    }

    /**
     * 画水滴
     */
    private void drawableWaterDrop(Canvas canvas) {
        if (mDropBitmap == null) {
            Drawable drawable = getResources().getDrawable(R.drawable.water_drop);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTintList(ColorStateList.valueOf(mWave1Color));
            }
//            mDropBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.water_drop);
            mDropBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas1 = new Canvas(mDropBitmap);
            drawable.setBounds(0, 0, canvas1.getWidth(), canvas1.getHeight());
            drawable.draw(canvas1);
        }
        if (mWaterDropHeight == 0 && mWaterDropWidth == 0) {
            mWaterDropWidth = mWidth / 8;
            mWaterDropHeight = mHeight / 8;
        }
        if (mSrcRect == null) {
            mSrcRect = new Rect();
            mSrcRect.left = 0;
            mSrcRect.top = 0;
            mSrcRect.right = mDropBitmap.getWidth();
            mSrcRect.bottom = mDropBitmap.getHeight();
        }
        if (mDestRect == null) {
            mDestRect = new Rect();
        }
        mDestRect.left = mWidth / 2 - mWaterDropWidth / 2;
        mDestRect.top = mTempDropHeight;
        mDestRect.right = mWidth / 2 + mWaterDropWidth / 2;
        mDestRect.bottom = mTempDropHeight + mWaterDropHeight;
        //画目标图像
        canvas.drawBitmap(mDropBitmap, mSrcRect, mDestRect, mBezierWavePaint);
        mCirclePaint.setStrokeWidth((mCircleGradientStroke + mInnerCircleDistance));
        mCirclePaint.setColor(Color.WHITE);
        //圆环白色圆环
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, mWidth / 2f - mCircleGradientStroke / 2f - mInnerCircleDistance / 2f, mCirclePaint);

    }

    /**
     * 画波浪
     */
    private void drawWave(Canvas canvas) {
        // modify paint shader according to mShowWave state
        if (mShowWave && mWaveShader != null) {
            // first call after mShowWave, assign it to our paint
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }
            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(
                    mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                    mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                    0,
                    mDefaultWaterLevel);
            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate(
                    mWaveShiftRatio * getWidth(),
                    (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            // assign matrix to invalidate the shader
            mWaveShader.setLocalMatrix(mShaderMatrix);

            float radius = mWidth / 2f - mCircleGradientStroke - mInnerCircleDistance;
            canvas.drawCircle(mWidth / 2f, mHeight / 2f, radius, mViewPaint);
        } else {
            mViewPaint.setShader(null);
        }

    }

    /**
     * drawText
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        float textWidth = mProgressTextPaint.measureText((int) (mWaterLevelRatio * 100) + "%");
        float textHeight = getFontHeight(mProgressTextPaint);
        canvas.drawText((int) (mWaterLevelRatio * 100) + "%", mWidth / 2 - textWidth / 2f,
                mHeight / 2 + textHeight / 4f, mProgressTextPaint);
    }

    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(mWave2Color);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(mWave1Color);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    public void startAnimation() {
        mValueAnimator = ValueAnimator.ofInt(0, 360);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRoteDegress = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.setDuration(360 * 5);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.start();
        Log.d("mDropAnimator: ", "mWidth: " + mWidth + " mHeight: " + mHeight);

        mDropAnimator = ValueAnimator.ofInt(0, mHeight - mCircleGradientStroke - mInnerCircleDistance);
        mDropAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTempDropHeight = (int) animation.getAnimatedValue();
                Log.d("mDropAnimator: ", "" + mTempDropHeight);
                postInvalidate();
            }
        });
        mDropAnimator.setInterpolator(new LinearInterpolator());
        mDropAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mDropAnimator.setRepeatMode(ValueAnimator.RESTART);
        mDropAnimator.setDuration(2000);
        mDropAnimator.start();

    }

    public void stopAnimation() {
        mValueAnimator.cancel();
        mDropAnimator.cancel();
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     * Shift the wave horizontally according to <code>waveShiftRatio</code>.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     *                       Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * Set water level according to <code>waterLevelRatio</code>.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     *                        Ratio of water level to WaveView height.
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    /**
     * Set vertical size of wave according to <code>amplitudeRatio</code>
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     *                       Ratio of amplitude to height of WaveView.
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    /**
     * Set horizontal size of wave according to <code>waveLengthRatio</code>
     *
     * @param waveLengthRatio Default to be 1.
     *                        Ratio of wave length to width of WaveView.
     */
    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return Math.abs(fm.descent - fm.ascent);
    }

}
