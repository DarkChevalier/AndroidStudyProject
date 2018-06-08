package com.sunzhibin.bezierlinechart.chart;

import android.graphics.PointF;

import java.util.List;

/**
 * Created by allever on 17-8-10.
 * 每一条线对应一个对象
 */

public class LineDataSet {
    private int color;//颜色，
    private int[] gradientColors;//渐变色数组
    private String unit;//参数类型
    private List<PointF> oldPointFLists;//参与计算的数据点

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

    public List<PointF> getOldPointFLists() {
        return oldPointFLists;
    }

    public void setOldPointFLists(List<PointF> oldPointFLists) {
        this.oldPointFLists = oldPointFLists;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
