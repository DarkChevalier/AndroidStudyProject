package com.sunzhibin.studyproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sunzhibin.studyproject.widget.RippleView;
import com.sunzhibin.studyproject.widget.WaveView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private List<String> mlist = new ArrayList<>();
    private WaveHelper mWaveHelper;
    private SensorManager sensorManager;
    private int currentDegree;
    private WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getBaseContext(), TestActivity.class));
//                ((AliPaySuccessView)findViewById(R.id.hookIcon)).startAnim();
//                ((RotateCircleView)findViewById(R.id.rotateCircleView)).startAnim();
                ((RippleView) findViewById(R.id.rippleView)).startAnim();
            }
        });
        waveView = findViewById(R.id.waveView);
        mWaveHelper = new WaveHelper(waveView);
        waveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWaveHelper.start();
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onResume() {
        if (sensorManager != null && sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        // 读取加速度传感器数值，values数组0,1,2分别对应x,y,z轴的加速度
//        Log.i("onSensorChanged", "onSensorChanged: " + event.values[0] + ", " + event.values[1] + ", " + event.values[2]);

        if (Sensor.TYPE_ACCELEROMETER != event.sensor.getType()) {
            return;
        }
        float[] values = event.values;
        float ax = values[0];
        float ay = values[1];
        //g :斜边长度
        double g = Math.sqrt(ax * ax + ay * ay);
        // y轴和合成方向的余弦： ay/g
        double cos = ay / g;
        if (cos > 1) {
            cos = 1;
        } else if (cos < -1) {
            cos = -1;
        }
        // y轴和合成方向的弧度： ay/g
        double rad = Math.acos(cos);
        if (ax < 0) {
            rad = 2 * Math.PI - rad;
        }
        try {

            int uiRot = getWindowManager().getDefaultDisplay().getRotation();
            double uiRad = Math.PI / 2 * uiRot;
            rad -= uiRad;
            int degrees = (int) (180 * rad / Math.PI);

            //如果度数变化不超过5，不变化，防止频繁晃动
            if (Math.abs(currentDegree - degrees) > 5) {
                if (degrees < 90) {
                    waveView.setmSensorDegrees(degrees);
                } else if (degrees > 270 && degrees < 360) {
                    waveView.setmSensorDegrees(degrees - 360);
                }
                Log.i("onSensorChanged", "currentDegree: " + currentDegree + " degrees: " + degrees);
                currentDegree = degrees;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
