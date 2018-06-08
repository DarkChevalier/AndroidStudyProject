package com.sunzhibin.studyproject.widget;

import android.content.Context;
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

import com.sunzhibin.studyproject.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/5/12.
 * description: description
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class LineChartView extends View {
    private final static String TAG = "LineChartView";
    private Paint mLineTextPaint;
    private Paint mBezierPaint;
    private Paint mRectPaint;
    private Paint mTextPaint;
    private Paint mVerticalPaint;
    private Paint mXPaint;
    private Paint mCirclePaint;
    private Paint mMarkPaint;
    private float mMarginLeftRight;
    private int mMarginTopBottom = 10;

    private float mWidth;
    private int mHeight;

    private List<String> mXNameListShow;
    private List<String> mXNameList;
    private List<LineDataSet> mLineDataSetList;
    private List<List<BezierLineData>> mBezierLineDataList;
    private List<List<PointF>> mOriginPointList;
    private List<PointF> mDataPointList;
    private int mBezierPointCount;
    private float mDownX;

    //
    private List<PointF> xvalPointF;//X轴每个刻度的坐标
    private PointF mMaxYPointF;//Y轴方向最大值坐标点
    private PointF mMinYPointF;
    private float mMaxYValue;//真实数据的最大值
    private float mMinYValue;
    private boolean isChoocePoint = true;
    private boolean isNeedPoint = true;
    //选中点
    float cx = -1;
    float cy = -1;
    //marker框的大小
    private float mMarkHeight = DensityUtil.dip2px(getContext(), 20);
    private float mMarkWidth = DensityUtil.dip2px(getContext(), 20);
    private int mPosition;

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mLineTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBezierPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mVerticalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mXPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mLineTextPaint.setDither(true);
        mBezierPaint.setDither(true);
        mRectPaint.setDither(true);
        mTextPaint.setDither(true);
        mVerticalPaint.setDither(true);
        mXPaint.setDither(true);
        mCirclePaint.setDither(true);
        mMarkPaint.setDither(true);

        mLineTextPaint.setAntiAlias(true);
        mBezierPaint.setAntiAlias(true);
        mRectPaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        mVerticalPaint.setAntiAlias(true);
        mXPaint.setAntiAlias(true);
        mCirclePaint.setAntiAlias(true);
        mMarkPaint.setAntiAlias(true);

        mBezierPaint.setStyle(Paint.Style.STROKE);
        mBezierPaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 3f));//设置线宽
        mBezierPaint.setAntiAlias(true);//去除锯齿
        mBezierPaint.setStrokeJoin(Paint.Join.ROUND);
        mBezierPaint.setStrokeCap(Paint.Cap.ROUND);

        mMarkPaint.setStyle(Paint.Style.FILL);
//        mLineTextPaint.setStrokeWidth(5);
//        mBezierPaint.setStrokeWidth(5);
//        mRectPaint.setStrokeWidth(5);
//        mTextPaint.setStrokeWidth(5);
//        mVerticalPaint.setStrokeWidth(5);

        mXNameListShow = new ArrayList<>();
        mXNameList = new ArrayList<>();
        mLineDataSetList = new ArrayList<>();
        mBezierLineDataList = new ArrayList<>();
        mOriginPointList = new ArrayList<>();
        mDataPointList = new ArrayList<>();
        xvalPointF = new ArrayList<>();
        mMaxYPointF = new PointF();
        mMinYPointF = new PointF();
        //设置X轴数据
        for (int i = 0; i < 7; i++) {
            mXNameList.add("12/0" + i);
        }
        setxNameDataList(mXNameList);
        //设置Y轴数据
        mDataPointList.add(new PointF(1, 10));
        mDataPointList.add(new PointF(2, 50));
        mDataPointList.add(new PointF(3, 30));
        mDataPointList.add(new PointF(4, 50));
        mDataPointList.add(new PointF(5, 20));
        mDataPointList.add(new PointF(6, 50));
        mDataPointList.add(new PointF(7, 10));
        setmLineDataSetList(mDataPointList);


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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        画5条分割线和x坐标
        drawHorizonLine(canvas);
        //画X坐标
        drawXLable(canvas);
        //绘制贝塞尔曲线
        drawBezier2(canvas);

        drawMarker(canvas);
    }


    /**
     * 画5条分割线
     */
    private void drawHorizonLine(Canvas canvas) {
//        mLineTextPaint.setColor(0x66cccccc);
        mLineTextPaint.setColor(0xFF000000);
        mLineTextPaint.setTextSize(DensityUtil.dip2px(getContext(), 12f));
        float intervalY = (getMeasuredHeight() - mMarginTopBottom * 2) / 4;//每条线段的间隔
        float textHeight = getFontHeight(mLineTextPaint);
        for (int i = 0; i < 5; i++) {
            canvas.drawLine(
                    mMarginLeftRight,
                    mHeight - textHeight - DensityUtil.dip2px(getContext(), 12f) - mMarginTopBottom - intervalY * i,
                    mWidth - mMarginLeftRight,
                    mHeight - textHeight - DensityUtil.dip2px(getContext(), 12f) - mMarginTopBottom - intervalY * i,
                    mLineTextPaint);
        }
        mMinYPointF = new PointF(mMarginLeftRight, mHeight - textHeight - DensityUtil.dip2px(getContext(), 12f) - mMarginTopBottom);
        mMaxYPointF = new PointF(mMarginLeftRight, mMarginTopBottom);
    }

    /**
     * 画横坐标
     */
    private void drawXLable(Canvas canvas) {
        xvalPointF.clear();
        mLineTextPaint.setTextSize(DensityUtil.dip2px(getContext(), 12f));
        mXPaint.setColor(0xFF000000);
        float textWidth = mLineTextPaint.measureText(mXNameListShow.get(0));
        float textHeight = getFontHeight(mLineTextPaint);
        if (mMarginLeftRight == 0 || mMarginLeftRight < textWidth / 2f) {
            mMarginLeftRight = textWidth / 2;
        }
        float xNameintervalX = (mWidth - 2f * mMarginLeftRight) / (mXNameListShow.size() - 1);//横坐标的间隔
        for (int i = 0; i < mXNameListShow.size(); i++) {
            canvas.drawText(mXNameListShow.get(i),
                    mMarginLeftRight + xNameintervalX * i - textWidth / 2f,
                    mHeight - DensityUtil.dip2px(getContext(), 10f),
                    mLineTextPaint);
            //x轴刻度线
            canvas.drawLine(mMarginLeftRight + xNameintervalX * i,
                    mHeight - textHeight - DensityUtil.dip2px(getContext(), 12f) - mMarginTopBottom,
                    mMarginLeftRight + xNameintervalX * i,
                    mHeight - textHeight - DensityUtil.dip2px(getContext(), 14f) - mMarginTopBottom,
                    mXPaint);
            xvalPointF.add(new PointF(mMarginLeftRight + xNameintervalX * i,
                    mHeight - textHeight - DensityUtil.dip2px(getContext(), 12f) - mMarginTopBottom));
        }
    }


    private void drawBezier2(Canvas canvas) {
        //绘制前先获取，保存一些数据，原始点(y值)，每条曲线每一段的数据点集,绘制标注时用到.
        initData();
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
                    canvas.drawCircle(cx, cy, DensityUtil.dip2px(getContext(), 5), mCirclePaint);
            } else if (isNeedPoint) {
                //绘制数据点未选中点的圆点
            }

        }
    }

    private void drawMarker(Canvas canvas) {
        if (cy != -1 && cx != -1) {
            mMarkPaint.setTextSize(10);
            canvas.drawText(mDataPointList.get(mPosition).y + "",
                    cx - mMarkPaint.measureText(mDataPointList.get(mPosition).y + "") / 2,
                    cy + getFontHeight(mMarkPaint) / 2 <= mMaxYPointF.y ? cy - getFontHeight(mMarkPaint) / 2 : cy + getFontHeight(mMarkPaint) / 2,
                    mMarkPaint);
            RectF rectF = new RectF();
            rectF.left = cx - mMarkWidth / 2;
            rectF.top = cy - mMarkHeight / 2 <= mMaxYPointF.y ? cy + mMarkHeight / 2 : cy - mMarkHeight / 2;
            rectF.right = cx + mMarkWidth / 2;
            rectF.bottom = cx + mMarkHeight / 2 == rectF.top ? cy + mMarkHeight : cx + mMarkHeight / 2;
            canvas.drawRoundRect(rectF, mMarkWidth / 10, mMarkWidth / 10, mMarkPaint);
        }
    }

    private void initData() {
        List<PointF> aOriginPointList;
        //List<PointF> aAndroidPointList;
        //List<BezierLineData> aLineDataList;
        //List<PointF> aSelectedOriginPointList;
        mBezierLineDataList.clear();
        mOriginPointList.clear();
        //mAndroidPointList.clear();
        for (int i = 0; i < mLineDataSetList.size(); i++) {
            //每一次遍历就是一条曲线数据
            LineDataSet lineDataSet = mLineDataSetList.get(i);
            aOriginPointList = lineDataSet.getOldPointFsList();
            if (aOriginPointList.size() == 0) continue;
            mOriginPointList.add(aOriginPointList);
            //lineDataSet.getOldPointFsList()获取原始坐标
            //getSelectedPoint();获取筛选后的数据
            //changePoint();将原始点转化为Android的坐标点
            //getLineData();获取贝塞尔曲线的点集
            mBezierLineDataList.add(
                    getLineData(
                            changePoint(i, lineDataSet.getOldPointFsList())));

        }

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
            p = new PointF(x, y);
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
    private List<BezierLineData> getLineData(List<PointF> pointList) {
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
     * 求曲线上的y坐标(Android坐标系)
     * B(t) = P0 * (1-t)^3 + 3 * P1 * t * (1-t)^2 + 3 * P2 * t^2 * (1-t) + P3 * t^3, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点1
     * @param p2 控制点2
     * @param p3 终止点
     * @return t对应的点
     */
    public static PointF calculateBezierPointForCubic(float t, PointF p0, PointF p1, PointF p2, PointF p3) {
        PointF point = new PointF();
        float temp = 1 - t;
        point.x = p0.x * temp * temp * temp + 3 * p1.x * t * temp * temp + 3 * p2.x * t * t * temp + p3.x * t * t * t;
        point.y = p0.y * temp * temp * temp + 3 * p1.y * t * temp * temp + 3 * p2.y * t * t * temp + p3.y * t * t * t;
        return point;
    }


    /**
     * mXNameListShow是横坐标的集合,默认10个以下的数据,通过外部获取
     * 设置底部时间数据
     */
    public void setxNameDataList(List<String> xNameDataList) {
        this.mXNameList = xNameDataList;
        setxNameListShow();
        postInvalidateDelayed(50);
    }

    /**
     * 设置显示的时间
     * 只显示10个时间点
     */
    private void setxNameListShow() {
        int interval = mXNameList.size() / 10 + 1;//只显示10个横坐标,的间隔
        for (int i = 0; i < mXNameList.size(); i = i + interval) {
            mXNameListShow.add(mXNameList.get(i));
        }
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
            lineDataSet.color = 0xFFFF0000;
            lineDataSet.gradientColors = new int[]{0xFFCB05FF, 0xFF231EFB, 0xFFCB05FF};
            lineDataSet.oldPointFsList = pointFLists;
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
        postInvalidate();
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
                mPosition = 0;
                invalidate();
                isChoocePoint = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: ACTION_MOVE mDownX = " + mDownX);
                mDownX = event.getX();
                invalidate();
                //postInvalidateDelayed(50);
                break;
            case MotionEvent.ACTION_UP:
                mPosition = 0;
                for (PointF p : xvalPointF) {
                    if (Math.abs(downX - p.x) <= (xvalPointF.get(1).x - xvalPointF.get(0).x) / 3) {
                        isChoocePoint = true;
                        cx = p.x;
                        cy = p.y;
                        invalidate();
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
     * Created by allever on 17-9-28.
     * <p>
     * 每一段曲线用到的数据点集
     */

    public class BezierLineData {
        private PointF startP;
        private PointF endP;
        private PointF cp1;
        private PointF cp2;

        public BezierLineData(PointF startP, PointF endP, PointF cp1, PointF cp2) {
            this.startP = startP;
            this.endP = endP;
            this.cp1 = cp1;
            this.cp2 = cp2;
        }

        public PointF getStartP() {
            return startP;
        }

        public void setStartP(PointF startP) {
            this.startP = startP;
        }

        public PointF getEndP() {
            return endP;
        }

        public void setEndP(PointF endP) {
            this.endP = endP;
        }

        public PointF getCp1() {
            return cp1;
        }

        public void setCp1(PointF cp1) {
            this.cp1 = cp1;
        }

        public PointF getCp2() {
            return cp2;
        }

        public void setCp2(PointF cp2) {
            this.cp2 = cp2;
        }
    }

    /**
     * Created by allever on 17-8-10.
     * 每一条线对应一个对象
     */

    public class LineDataSet {
        private int color;//颜色，
        private int[] gradientColors;//渐变色数组
        private List<PointF> oldPointFsList;//原始点
//        private SportAnalysisType sportAnalysisType;//参数类型，项目中用到，实际上该字段无用

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int[] getGradientColors() {
            return gradientColors;
        }

        public void setGradientColors(int[] gradientColors) {
            this.gradientColors = gradientColors;
        }

        public List<PointF> getOldPointFsList() {
            return oldPointFsList;
        }

        public void setOldPointFsList(List<PointF> oldPointFsList) {
            this.oldPointFsList = oldPointFsList;
        }

    }

    private float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return Math.abs(fm.descent - fm.ascent);
    }

}
