package com.ark.sample;

import android.app.Application;
import com.ark.adkit.basics.configs.Strategy;
import com.ark.adkit.basics.utils.JsonUtils;
import com.ark.adkit.polymers.polymer.ADTool;

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
}
