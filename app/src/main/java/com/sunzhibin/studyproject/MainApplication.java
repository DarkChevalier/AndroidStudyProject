package com.sunzhibin.studyproject;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * @author: sunzhibin
 * <p>
 * date: 2018/4/21.
 * description: description
 * e-mail: E-mail
 * modify： the history
 * </p>
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupLeakCanary();
    }

    protected RefWatcher setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }
}
