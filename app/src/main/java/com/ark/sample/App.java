package com.ark.sample;

import android.app.Application;
import com.ark.adkit.basics.configs.Strategy;
import com.ark.adkit.basics.utils.JsonUtils;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.net.urlconn.GsonConverter;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.KalleConfig;
import com.yanzhenjie.kalle.connect.BroadcastNetwork;
import com.yanzhenjie.kalle.connect.http.LoggerInterceptor;
import com.yanzhenjie.kalle.cookie.DBCookieStore;
import com.yanzhenjie.kalle.urlconnect.URLConnectionFactory;

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
        ADTool.initialize(new ADTool.Builder()
                .setDebugMode(true)
                .setStrategy(Strategy.cycle)
                .setLoadOtherWhenVideoDisable(false)
                .setLocalConfig(JsonUtils.getJson(this,"config.json"))
                .build());
    }

    public void initialize() {

    }
}
