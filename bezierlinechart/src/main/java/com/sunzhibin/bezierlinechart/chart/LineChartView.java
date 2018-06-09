package com.sunzhibin.bezierlinechart.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.sunzhibin.bezierlinechart.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/5/12.
 * description: 渐进色贝塞尔曲线
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class LineChartView extends View {
    private final static String TAG = "LineChartView";
    //画 X/Y轴
    private Paint mXLineTextPaint;
    //    画 Y轴
//    private Paint mYLinePaint;
    private Paint mBezierPaint;
    private Paint mXPaint;
    private Paint mYPaint;
    private Paint mCirclePaint;
    private Paint mMarkPaint;
    private float mMarginLeftRight;
    private int mMarginTopBottom = 10;

    //控件宽高
    private float mWidth;
    private int mHeight;

    private List<String> mXNameList;
    private List<LineDataSet> mLineDataSetList;
    private List<List<BezierLineData>> mBezierLineDataList;
    private List<PointF> mOriginDataList;
    private List<PointF> mNewDataList;//转变Android坐标后的数据
    private float mDownX;
    //
    private List<PointF> xvalPointF;//X轴每个刻度的坐标
    private PointF mMaxYPointF;//Y轴方向最大值坐标点
    private PointF mMinYPointF;
    private float mMaxYValue;//真实数据的最大值
    private float mMinYValue;
    private boolean isChoocePoint = false;
    private boolean isNeedPoint = false;
    //选中点
    float cx = -1;
    float cy = -1;
    //marker框的大小
    private float mMarkHeight = dip2px(getContext(), 20);
    private float mMarkWidth = dip2px(getContext(), 20);
    private int mMarkColor = 0xFFCB05FF;
    private int mPosition = -1;
    private int[] mGradientColors = new int[]{getResources().getColor(R.color.color_gradient_origin),
            getResources().getColor(R.color.color_gradient_origin2),
            getResources().getColor(R.color.color_gradient_origin)};

    private int mMarkTextSize = 12;
    private int mYTextSize = 12;
    private int mXTextSize = 12;
    private int mYTextColor = getResources().getColor(R.color.color_text_gray_hint);
    private int mXTextColor = getResources().getColor(R.color.color_text_gray_hint);
    private int mLineTextColor = 0x66cccccc;
    private int mXLineTextColor = 0x66cccccc;
    private int mBezierLineWidth = 3;
    private int mBgLineWidth = 2;
    private String mCurrentData = "";
    // y轴字距离Y轴的距离
    private float textYpaddingY = dip2px(getContext(), 5);
    //x轴字距离x轴距离
    private float textXpaddingX = dip2px(getContext(), 0);
    private float textXBottomX = dip2px(getContext(), 10f);
    //是否需要画刻度线
    private boolean isNeedYRuling = false;
    private boolean isNeedXRuling = false;

    private Scroller mScroller;
    //每页展示做多的数据，一般7条
    private final static int mMaxShowSize = 7;
    private float mTempDistance = -200;

    public LineChartView(Context context) {
        super(context);
        initAtrrs(context, null);
        init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAtrrs(context, attrs);
        init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAtrrs(context, attrs);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAtrrs(context, attrs);
        init();
    }

    private void initAtrrs(Context context, AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LineChartView);
        mMaxYValue = (int) a.getDimension(R.styleable.LineChartView_maxvalue, mMaxYValue);
        mMinYValue = (int) a.getDimension(R.styleable.LineChartView_minvalue, mMinYValue);
        mMarkHeight = (int) a.getDimension(R.styleable.LineChartView_markHeight, dip2px(context, mMarkHeight));
        mBezierLineWidth = (int) a.getDimension(R.styleable.LineChartView_bezier_line_width, dip2px(context, mBezierLineWidth));
        mBgLineWidth = (int) a.getDimension(R.styleable.LineChartView_bg_line_width, dip2px(context, mBgLineWidth));

        mMarkWidth = (int) a.getDimension(R.styleable.LineChartView_markWidth, dip2px(context, mMarkWidth));
        mMarkTextSize = (int) a.getDimension(R.styleable.LineChartView_markTextSize, dip2px(context, mMarkTextSize));
        mXTextSize = (int) a.getDimension(R.styleable.LineChartView_xTextSize, dip2px(context, mXTextSize));
        mYTextSize = (int) a.getDimension(R.styleable.LineChartView_yTextSize, dip2px(context, mYTextSize));

        mYTextColor = a.getColor(R.styleable.LineChartView_yTextColor, mYTextColor);
        mXTextColor = a.getColor(R.styleable.LineChartView_xTextColor, mXTextColor);
        mLineTextColor = a.getColor(R.styleable.LineChartView_lineTextColor, mLineTextColor);
        mXLineTextColor = a.getColor(R.styleable.LineChartView_xLineTextColor, mXLineTextColor);
        a.recycle();

    }

    private void init() {
        mXLineTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBezierPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mXPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mYPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mXLineTextPaint.setDither(true);
        mBezierPaint.setDither(true);
        mXPaint.setDither(true);
        mCirclePaint.setDither(true);
        mMarkPaint.setDither(true);
        mYPaint.setDither(true);

        mXLineTextPaint.setAntiAlias(true);
        mBezierPaint.setAntiAlias(true);
        mXPaint.setAntiAlias(true);
        mCirclePaint.setAntiAlias(true);
        mMarkPaint.setAntiAlias(true);
        mYPaint.setAntiAlias(true);

        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setStrokeWidth(mBezierLineWidth);//设置线宽
        mBezierPaint.setAntiAlias(true);//去除锯齿
        mBezierPaint.setStrokeJoin(Paint.Join.ROUND);
        mBezierPaint.setStrokeCap(Paint.Cap.ROUND);

        mMarkPaint.setStyle(Paint.Style.FILL);
        mMarkPaint.setTextSize(mMarkTextSize);
        mMarkPaint.setColor(mMarkColor);

        mXPaint.setTextSize(mXTextSize);
        mYPaint.setTextSize(mYTextSize);

        mYPaint.setColor(mYTextColor);
        mXPaint.setColor(mXTextColor);
        mXLineTextPaint.setColor(mXLineTextColor);

        mXNameList = new ArrayList<>();
        mLineDataSetList = new ArrayList<>();
        mBezierLineDataList = new ArrayList<>();
        mOriginDataList = new ArrayList<>();
        xvalPointF = new ArrayList<>();
        mNewDataList = new ArrayList<>();

        mMaxYPointF = new PointF();
        mMinYPointF = new PointF();

        mScroller = new Scroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        if (mMarginTopBottom == 0)
            mMarginTopBottom = getPaddingBottom();
        if (mMarginLeftRight == 0)
            mMarginLeftRight = getPaddingLeft();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHorizonLine(canvas);
        drawXLable(canvas);
        drawBezier(canvas);
        drawMark(canvas);
    }

    /**
     * 画5条分割线
     */
    private void drawHorizonLine(Canvas canvas) {
        float intervalY = (getMeasuredHeight() - mMarginTopBottom * 2) / 4;//每条线段的间隔
        float textHeight = getFontHeight(mXPaint);
        float textWidth = mYPaint.measureText(mMaxYValue + "");
        float textHeight2 = getFontHeight(mYPaint);
        if (mMarginLeftRight < textWidth) {
            mMarginLeftRight = textWidth + textYpaddingY;
        }
//        mYPaint.setStrokeWidth(mBgLineWidth);
//        mYPaint.setColor(0x66cccccc);
        for (int i = 0; i < 5; i++) {
            //绘制X轴
            if (i == 0) {
                canvas.drawLine(
                        mMarginLeftRight - textYpaddingY / 2,
                        mHeight - textHeight - textXpaddingX - textXBottomX - intervalY * i,
                        mWidth,
                        mHeight - textHeight - textXpaddingX - textXBottomX - intervalY * i,
                        mXLineTextPaint);
            } else if (isNeedXRuling) {
                //绘制Y轴上x方向的刻度线
                canvas.drawLine(
                        mMarginLeftRight + textYpaddingY,
                        mHeight - textHeight - textXpaddingX - textXBottomX - intervalY * i,
                        mWidth,
                        mHeight - textHeight - textXpaddingX - textXBottomX - intervalY * i,
                        mXLineTextPaint);
            }
            if (i == 0) {
                float tempWidth = mYPaint.measureText(mMaxYValue + "");
                textWidth = mYPaint.measureText(i * (mMaxYValue - mMinYValue) + "");
                textWidth += tempWidth / 2f - textWidth / 2f;
            } else {
                textWidth = mYPaint.measureText(i * (mMaxYValue - mMinYValue) + "");
            }
            canvas.drawText(i * (mMaxYValue - mMinYValue) + "", mMarginLeftRight - textWidth,
                    mHeight - textHeight - textXBottomX - textXpaddingX - intervalY * i + textHeight2 / 4
                    , mYPaint);
        }
        mMinYPointF = new PointF(mMarginLeftRight,
                mHeight - textHeight - dip2px(getContext(), 5f) - mMarginTopBottom);
        mMaxYPointF = new PointF(mMarginLeftRight, mMarginTopBottom);
        changePoint(mOriginDataList);

    }

    /**
     * 画横坐标、Y轴
     */
    private void drawXLable(Canvas canvas) {
        xvalPointF.clear();
        if (mXNameList.size() == 0) return;
        float textWidth = mXPaint.measureText(mXNameList.get(0));
        float textHeight = getFontHeight(mXPaint);
//        if (mMarginLeftRight == 0 || mMarginLeftRight < textWidth / 2f) {
//            mMarginLeftRight = textWidth / 2;
//        }
        float xNameintervalX = (mWidth - mMarginLeftRight - textWidth - textYpaddingY) / (mMaxShowSize);//横坐标的间隔
        for (int i = 0; i < mXNameList.size(); i++) {
            canvas.drawText(mXNameList.get(i),
                    mMarginLeftRight + xNameintervalX * i + textYpaddingY + mTempDistance,
                    mHeight - textXBottomX,
                    mXPaint);
            //绘制Y轴
            if (i == 0) {
                canvas.drawLine(mMarginLeftRight + xNameintervalX * i + textYpaddingY,
                        mHeight - textHeight - textXpaddingX - textXBottomX,
                        mMarginLeftRight + xNameintervalX * i + textYpaddingY,
                        0 + mMarginTopBottom,
                        mXLineTextPaint);
            }
            //绘制x轴上的Y方向的刻度线
            if (isNeedYRuling) {
                canvas.drawLine(mMarginLeftRight + xNameintervalX * i + textYpaddingY + textWidth / 2,
                        mHeight - textHeight - textXpaddingX - textXBottomX,
                        mMarginLeftRight + xNameintervalX * i + textYpaddingY + textWidth / 2,
                        0 + mMarginTopBottom,
                        mXLineTextPaint);
            } else {
                canvas.drawLine(mMarginLeftRight + xNameintervalX * i + textYpaddingY + textWidth / 2,
                        mHeight - textHeight - textXpaddingX - textXBottomX,
                        mMarginLeftRight + xNameintervalX * i + textYpaddingY + textWidth / 2,
                        mHeight - textHeight - textXpaddingX - textXBottomX - dip2px(getContext(),2),
                        mXLineTextPaint);
            }
            xvalPointF.add(new PointF(mMarginLeftRight + xNameintervalX * i + textYpaddingY + textWidth / 2,
                    mHeight - textHeight - textXpaddingX - textXBottomX));

        }
    }

    private void drawBezier(Canvas canvas) {
        for (int i = 0; i < mLineDataSetList.size(); i++) {
            LineDataSet lineDataSet = mLineDataSetList.get(i);
            mBezierLineDataList.add(
                    bezierLineData(
                            changePoint(i, lineDataSet.getOldPointFLists())));
        }
        for (int i = 0; i < mLineDataSetList.size(); i++) {
            Path bezierPath = new Path();//曲线路径
            bezierPath.moveTo(mBezierLineDataList.get(i).get(0).getStartP().x, mBezierLineDataList.get(i).get(0).getStartP().y);
            for (int j = 0; j < mBezierLineDataList.get(i).size(); j++) {
                bezierPath.cubicTo(
                        mBezierLineDataList.get(i).get(j).getCp1().x, mBezierLineDataList.get(i).get(j).getCp1().y,
                        mBezierLineDataList.get(i).get(j).getCp2().x, mBezierLineDataList.get(i).get(j).getCp2().y,
                        mBezierLineDataList.get(i).get(j).getEndP().x, mBezierLineDataList.get(i).get(j).getEndP().y);
            }
            //设置颜色和渐变
            int lineColor = mLineDataSetList.get(i).getColor();
            mBezierPaint.setColor(lineColor);
            LinearGradient mLinearGradient;
            int[] colorArr;
            if (mLineDataSetList.get(i).getGradientColors() != null) {
                colorArr = mLineDataSetList.get(i).getGradientColors();
            } else {
                colorArr = new int[]{lineColor, lineColor, lineColor, lineColor, lineColor};
            }
            mLinearGradient = new LinearGradient(
                    mBezierLineDataList.get(i).get(0).getStartP().x,
                    0,
                    mBezierLineDataList.get(i).get(0).getEndP().x,
                    0,
                    colorArr,
                    null,
                    Shader.TileMode.CLAMP
            );
            mBezierPaint.setShader(mLinearGradient);
            canvas.drawPath(bezierPath, mBezierPaint);
            canvas.save();

            if (isChoocePoint && isNeedPoint) {
                //绘制数据点未选中点的圆点
                mCirclePaint.setColor(Color.RED);
                mCirclePaint.setStrokeWidth(1);
                mCirclePaint.setStyle(Paint.Style.FILL);
                if (cy != -1 && cy != -1)
                    canvas.drawCircle(cx, cy, dip2px(getContext(), 5), mCirclePaint);
            } else if (isNeedPoint) {
                //绘制数据点未选中点的圆点
            }

        }


    }

    /**
     * @param canvas
     */
    private void drawMark(Canvas canvas) {

        if (mPosition == -1 || mPosition == mOriginDataList.size()) return;
        mMarkPaint.setTextSize(dip2px(getContext(), 12));

        float width = mMarkPaint.measureText(mCurrentData);
        float height = getFontHeight(mMarkPaint);

        RectF rectF = new RectF();
        rectF.left = cx - width;
        rectF.top = cy - height;
        rectF.right = cx + width;
        rectF.bottom = cy + height;
        mMarkPaint.setColor(mMarkColor);
        canvas.drawRoundRect(rectF, 10, 10, mMarkPaint);
        mMarkPaint.setColor(Color.WHITE);
        canvas.drawText(mCurrentData, cx - width / 2, cy + height / 4, mMarkPaint);

    }

    /**
     * 把数据点转为 Android中的视图坐标
     *
     * @param oldPointFs
     * @return
     */
    private List<PointF> changePoint(int index, List<PointF> oldPointFs) {
        List<PointF> pointFs = new ArrayList<>();
        float maxValueY = 0;
        float yValue;
        PointF p;
        float x;
        float y;
        float totalHeight = mMinYPointF.y - mMaxYPointF.y - 2 * mMarginTopBottom;
        float dataLenght = mMaxYValue;
        for (int i = 0; i < oldPointFs.size(); i++) {
            PointF pointF = oldPointFs.get(i);
            //最后的正负值是左移右移
            x = xvalPointF.get(index).x + i * (xvalPointF.get(1).x - xvalPointF.get(0).x);
            y = mMinYPointF.y - totalHeight * pointF.y / dataLenght + mMarginTopBottom;
            p = new PointF(x + mTempDistance, y);
            pointFs.add(p);
        }
        return pointFs;
    }

    /**
     * 利用三阶贝塞尔曲线，获取每一段曲线所需要的点集，包括开始点，结束点，两个控制点。
     *
     * @param pointList 所有的数据点
     * @return
     */
    private List<BezierLineData> bezierLineData(List<PointF> pointList) {
        float t = 0.5f;//比例
        List<BezierLineData> lineDataList = new ArrayList<>();
        PointF startP;
        PointF endP;
        PointF cp1;
        PointF cp2;
        BezierLineData lineData;
        for (int i = 0; i < pointList.size() - 1; i++) {
            startP = pointList.get(i);
            endP = pointList.get(i + 1);

            cp1 = new PointF();//控制点在水平方向，
            cp1.x = startP.x + (endP.x - startP.x) * t;
            cp1.y = startP.y;

            cp2 = new PointF();
            cp2.x = startP.x + (endP.x - startP.x) * (1 - t);
            cp2.y = endP.y;
            lineData = new BezierLineData(startP, endP, cp1, cp2);
            lineDataList.add(lineData);
        }
        return lineDataList;
    }

    /**
     * mXNameListShow是横坐标的集合,默认10个以下的数据,通过外部获取
     * 设置底部时间数据
     */
    public void setxDataList(List<String> xNameDataList) {
        if (xNameDataList.size() == 0) {
            return;
        }
        this.mXNameList = xNameDataList;
        float textWidth = mXPaint.measureText(mXNameList.get(0));
        if (mMarginLeftRight == 0 || mMarginLeftRight < textWidth / 2f) {
            mMarginLeftRight = textWidth / 2;
        }

        this.mXNameList = xNameDataList;
    }

    public void setYDataList(List<PointF> points) {
        if (points.size() == 0) return;
        this.mOriginDataList = points;
        setmLineDataSetList(mOriginDataList);
    }

    /**
     * 设置Y轴数据
     *
     * @param points
     */
    private void setmLineDataSetList(List<PointF> points) {
        this.mLineDataSetList.clear();
        List<PointF> pointFLists;
        LineDataSet lineDataSet;
        for (int i = 0; i < points.size() - 1; i++) {
            lineDataSet = new LineDataSet();
            pointFLists = new ArrayList<>();
            pointFLists.add(points.get(i));
            if (i == points.size() - 1) {
                pointFLists.add(points.get(i));
            } else {
                pointFLists.add(points.get(i + 1));
            }
            lineDataSet.setColor(0xFFFF0000);
            lineDataSet.setGradientColors(mGradientColors);
            lineDataSet.setOldPointFLists(pointFLists);
            mLineDataSetList.add(lineDataSet);

            if (mMinYValue == 0) {
                mMinYValue = points.get(i).y;
            } else if (points.get(i).y < mMinYValue) {
                mMinYValue = points.get(i).y;
            }

            if (mMaxYValue == 0) {
                mMaxYValue = points.get(i).y;
            } else if (points.get(i).y > mMaxYValue) {
                mMaxYValue = points.get(i).y;
            }

        }
        if (mMaxYValue == 0) {
            mMaxYValue = 1;
        }
        float textWidth = mYPaint.measureText(mMaxYValue + "");
        if (mMarginLeftRight == 0 || mMarginLeftRight < textWidth / 2f) {
            mMarginLeftRight = textWidth / 2;
        }
        postInvalidate();
    }

    /**
     * 把数据点转为 Android中的视图坐标
     *
     * @param oldPointFs
     * @return
     */
    private List<PointF> changePoint(List<PointF> oldPointFs) {
        mNewDataList.clear();
        float maxValueY = 0;
        float yValue;
        PointF p;
        float x;
        float y;
        float totalHeight = mMinYPointF.y - mMaxYPointF.y - 2 * mMarginTopBottom;
        float dataLenght = mMaxYValue;
        for (int i = 0; i < oldPointFs.size(); i++) {
            PointF pointF = oldPointFs.get(i);
            //最后的正负值是左移右移
            x = i;
            y = mMinYPointF.y - totalHeight * pointF.y / dataLenght + mMarginTopBottom;
            p = new PointF(x, y);
            mNewDataList.add(p);
        }
        return mNewDataList;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float downX = event.getX();
        float downY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = downX;
                cy = -1;
                cx = -1;
                //postInvalidateDelayed(50);
                Log.d(TAG, "onTouchEvent: ACTION_DOWN mDownX = " + mDownX);
                mPosition = -1;
                isChoocePoint = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ACTION_MOVE mDownX = " + mDownX);
                float tempX = event.getX();

//                mTempDistance += tempX - mDownX;

                invalidate();
                //postInvalidateDelayed(50);
                break;
            case MotionEvent.ACTION_UP:
                mPosition = 0;
                for (PointF p : mNewDataList) {
                    if (Math.abs(downX - xvalPointF.get(mPosition).x) <= dip2px(getContext(), 10)
                            && Math.abs(downY - p.y) <= dip2px(getContext(), 10)) {
                        isChoocePoint = true;
                        cx = xvalPointF.get(mPosition).x;
                        cy = p.y;
                        mCurrentData = mOriginDataList.get(mPosition).y + "";
                        Log.d("onTouchEvent: ", "mPosition: " + mPosition + " mCurrentData: " + mCurrentData);
                        postInvalidate();
                        continue;
                    }
                    mPosition++;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                cx = -1;
                cy = -1;
                invalidate();
                break;
        }
        //postInvalidateDelayed(50);
        return true;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return Math.abs(fm.descent - fm.ascent);
    }
}
