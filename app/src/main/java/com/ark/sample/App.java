package com.ark.sample;

import android.app.Application;

import com.ark.adkit.basics.configs.Strategy;
import com.ark.adkit.basics.utils.JsonUtils;
import com.ark.adkit.polymers.polymer.ADTool;
import com.squareup.leakcanary.LeakCanary;

public class App extends Application {

    private static App _instance;

    public static App get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (_instance == null) {
            _instance = this;
        }
        initLeakCanary();
        initialize();
    }

    public void initialize() {
        ADTool.initialize(new ADTool.Builder()
                .setDebugMode(true)
                .setStrategy(Strategy.cycle)
                .setLoadOtherWhenVideoDisable(true)
                .setLocalConfig(JsonUtils.getJson(this, "config.json"))
                .build());
    }


    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
