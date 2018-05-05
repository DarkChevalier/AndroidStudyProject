package com.sunzhibin.studyproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sunzhibin.studyproject.widget.RippleView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> mlist = new ArrayList<>();

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
