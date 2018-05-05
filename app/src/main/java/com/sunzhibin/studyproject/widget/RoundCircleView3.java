package com.sunzhibin.studyproject.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/4/26.
 * description: description
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
@SuppressLint("AppCompatCustomView")
public class RoundCircleView3 extends ImageView {

    private float width, height;

    public RoundCircleView3(Context context) {
        super(context);
    }

    public RoundCircleView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundCircleView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundCircleView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width > 12 && height > 12) {
            Path path = new Path();
            path.moveTo(12, 0);
            path.lineTo(width - 12, 0);
            path.quadTo(width, 0, width, 12);
            path.lineTo(width, height - 12);
            path.quadTo(width, height, width - 12, height);
            path.lineTo(12, height);
            path.quadTo(0, height, 0, height - 12);
            path.lineTo(0, 12);
            path.quadTo(0, 0, 12, 0);
            canvas.clipPath(path);
        }
    }

}
