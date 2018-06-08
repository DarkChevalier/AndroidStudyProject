package com.sunzhibin.studyproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sunzhibin.studyproject.widget.RippleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<String> mlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wiseMusicSetAreaControl(1, 11, 1);

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

    public byte[] wiseMusicSetAreaControl(int area, int voice, int open) {
        byte ch1 = 0x7e;
        byte ch2 = 0x7e;
        byte ch3 = 0x0;
        byte ch4 = 0xc;
        byte ch5 = (byte) 0xdd;//(byte) 0xdd;
        byte ch6 = (byte) (0x30 + area);

        byte ch7 = 0x3a;
        byte ch8 = 0x3a;

        byte ch9 = (byte) (0x30 + (voice > 10 ? 1 : 0));
        byte ch10 = (byte) (0x30 + (voice > 10 ? voice - 10 : voice));

        byte ch11 = 0x3a;
        byte ch12 = 0x3a;

        byte ch13 = (byte) (0x30 + open);
        byte ch14 = 0x1;
        byte ch15 = 0xd;
        byte ch16 = 0xa;
        byte ch[] = {ch6, ch7, ch8, ch9, ch10, ch11, ch12, ch13};
        byte chs[] = {ch1, ch2, ch3, ch4, ch5, ch6, ch7, ch8, ch9, ch10, ch11, ch12, ch13, ch14, ch15, ch16};
        Log.d("test", "wiseMusicSetAreaControl:" + Arrays.toString(ch) + "\n" + new String(ch));
        return ch;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
