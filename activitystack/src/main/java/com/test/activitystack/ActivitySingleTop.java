package com.test.activitystack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/5/24.
 * description: description
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class ActivitySingleTop extends BaseActivity {
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_show_text = findViewById(R.id.tv_show_text);
        tv_show_text.setText("栈顶复用");
        tv_show_text.setTextColor(getResources().getColor(R.color.colorPrimary));

        initView();
        onclick();
    }

    private void initView() {
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);


    }

    private void onclick() {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ActivitySingleInstance.class));
                startActivity(new Intent(getBaseContext(), ActivitySingleInstance.class));
                startActivity(new Intent(getBaseContext(), ActivitySingleInstance.class));
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ActivitySingTask.class));
                startActivity(new Intent(getBaseContext(), ActivitySingTask.class));
                startActivity(new Intent(getBaseContext(), ActivitySingTask.class));
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ActivitySingleTop.class));
                startActivity(new Intent(getBaseContext(), ActivitySingleTop.class));
                startActivity(new Intent(getBaseContext(), ActivitySingleTop.class));
            }
        });


    }
}
