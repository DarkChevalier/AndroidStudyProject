package com.sunzhibin.bezierlinechart;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sunzhibin.bezierlinechart.chart.LineChartView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LineChartView lineChartView = findViewById(R.id.lineChartView);

        List<String> xList = new ArrayList<>();
        List<PointF> yList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            xList.add("" + i);
            yList.add(new PointF(i, (int) (Math.random() * 100)));
        }
        lineChartView.setxDataList(xList);
        lineChartView.setYDataList(yList);

    }


}
