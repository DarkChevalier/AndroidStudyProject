package com.sunzhibin.studyproject;

/**
 * Created by RenHuan on 2017/2/28.
 */

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

public class AppManager {
    private static Stack<Activity> sActivityStack;
    private static AppManager sInstance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (sInstance == null) {
            sInstance = new AppManager();
        }
        return sInstance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
//        WeakReference<Activity> weakReference = new WeakReference(activity);
        if (sActivityStack == null) {
            sActivityStack = new Stack<>();
        }
//        if (weakReference.get() != null)
            sActivityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = sActivityStack.lastElement();
        return activity;
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
//        WeakReference weakReference = new WeakReference(activity);
        if (activity != null) {
//            if (weakReference.get() != null)
                sActivityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : sActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = sActivityStack.size(); i < size; i++) {
            if (null != sActivityStack.get(i)) {
                sActivityStack.get(i).finish();
            }
        }
        sActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());

        } catch (Exception e) {
        }
    }

    /**
     * 得到指定类名的Activity
     */
    public Activity getActivity(Class<?> cls) {
        for (Activity activity : sActivityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }
}
