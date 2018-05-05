package com.sunzhibin.studyproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.sunzhibin.studyproject.widget.TouchPullView;

public class TestActivity extends AppCompatActivity {
    private float mTouchMoveStartY;
    private final static float TOUCH_MOVE_MAX_Y = 600;
    private TouchPullView mTouchPullView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        handler.sendEmptyMessageDelayed(0, 5000);
        AppManager.getAppManager().addActivity(this);
        initListener();
        initView();
    }

    private void initView() {
        mTouchPullView = findViewById(R.id.touchPullView);
    }

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().finishActivity(this);
        super.onDestroy();

    }

    private void initListener() {
        findViewById(R.id.layout_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchMoveStartY = event.getY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float y = event.getY();
                        if (y >= mTouchMoveStartY) {
                            float moveSize = y - mTouchMoveStartY;
                            float progress = moveSize >= TOUCH_MOVE_MAX_Y
                                    ? 1 : moveSize / TOUCH_MOVE_MAX_Y;
                            mTouchPullView.setmProgress(progress);
                            return true;
                        }


                    case MotionEvent.ACTION_UP:

                        break;


                }
                return false;
            }
        });

    }
}
