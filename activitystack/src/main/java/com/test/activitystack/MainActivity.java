package com.test.activitystack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_show_text = findViewById(R.id.tv_show_text);
        tv_show_text.setText("标准模式");
        tv_show_text.setTextColor(getResources().getColor(R.color.colorAccent));

        initView();
        onclick();
        test();
    }

    private void initView() {
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
    }


    private void test() {
        List<String> listA = new ArrayList<>();
        List<String> listB = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            listA.add(i + "");
            if (i % 2 == 0)
                listB.add(i + "");
        }

        boolean a = listA.containsAll(listB);
        boolean b = listB.containsAll(listA);
        Log.d("test: ", " a: " + a + " b " + b);

    }

    private void onclick() {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
//                startActivity(new Intent(getBaseContext(), MainActivity.class));
//                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ActivitySingleInstance.class));
//                startActivity(new Intent(getBaseContext(), ActivitySingleInstance.class));
//                startActivity(new Intent(getBaseContext(), ActivitySingleInstance.class));
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ActivitySingTask.class));
//                startActivity(new Intent(getBaseContext(), ActivitySingTask.class));
//                startActivity(new Intent(getBaseContext(), ActivitySingTask.class));
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ActivitySingleTop.class));
//                startActivity(new Intent(getBaseContext(), ActivitySingleTop.class));
//                startActivity(new Intent(getBaseContext(), ActivitySingleTop.class));
            }
        });


    }


}
