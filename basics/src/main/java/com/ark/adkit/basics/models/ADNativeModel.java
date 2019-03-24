package com.ark.adkit.basics.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.ark.adkit.basics.configs.ADOnlineConfig;
import com.ark.adkit.basics.utils.LogUtils;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ADNativeModel {

    private static long TIME_INTERVAL = 1000;
    protected ADOnlineConfig mConfig;
    protected ConcurrentLinkedQueue<Object> linkedQueue = new ConcurrentLinkedQueue<>();
    private long lastLoadingTime;
    private boolean isInit;

    public void init(@Nullable ADOnlineConfig adOnlineConfig) {
        if (adOnlineConfig == null) {
            LogUtils.w("--->init Data error state ADOnlineConfig is null");
            return;
        }
        if (isInit) {
            return;
        }
        this.mConfig = adOnlineConfig;
        final String appKey = adOnlineConfig.appKey;
        final String subKey = adOnlineConfig.subKey;
        if (TextUtils.isEmpty(appKey) || TextUtils.isEmpty(subKey)) {
            LogUtils.w(
                    String.format("%1$s init Data error ,appKey:%2$s,subKey:%3$s", mConfig.platform,
                            appKey, subKey));
            return;
        }
        LogUtils.w(
                String.format("%1$s init Data success ,appKey:%2$s,subKey:%3$s", mConfig.platform,
                        appKey, subKey));
        isInit = true;
    }

    public abstract void loadData(@Nullable Context context, int count);

    @Nullable
    public abstract Object getData(@Nullable Context context);

    public void onCleared() {
        linkedQueue.clear();
    }

    public synchronized boolean isFast() {
        long now = System.currentTimeMillis();
        if (now - lastLoadingTime >= TIME_INTERVAL) {
            lastLoadingTime = now;
            return false;
        }
        return true;
    }

    public void handleSuccess(@NonNull final String platform, @Nullable final List list) {
        if (list == null || list.isEmpty()) {
            LogUtils.w(platform + "请求广告成功,但无可用广告返回");
            return;
        }
        for (Object o : list) {
            linkedQueue.offer(o);
        }
        LogUtils.w(platform + "请求广告成功,获取了" + list.size() + "条");
    }

    public void handleFailure(@NonNull String platform, int code, @Nullable String msg) {
        LogUtils.w(platform + "请求广告失败,code:" + code + ",msg:" + msg);
        TIME_INTERVAL += 200;
    }
}

