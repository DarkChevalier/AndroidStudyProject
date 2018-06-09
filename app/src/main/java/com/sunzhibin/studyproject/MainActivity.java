package com.sunzhibin.studyproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sunzhibin.studyproject.widget.RippleView;
import com.sunzhibin.studyproject.widget.WaveView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> mlist = new ArrayList<>();
    private WaveHelper mWaveHelper;

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
        final WaveView waveView = findViewById(R.id.waveView);
        mWaveHelper = new WaveHelper(waveView);
        waveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWaveHelper.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
