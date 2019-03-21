package com.ark.adkit.polymers.zhaocai;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.ark.adkit.basics.configs.ADOnlineConfig;
import com.ark.adkit.basics.models.ADNativeModel;
import com.ark.adkit.basics.utils.LogUtils;
import com.zhaocai.ad.sdk.*;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ADNativeModelOfZhaoCai extends ADNativeModel {

    private AdConfiguration adConfiguration;
    private ZhaoCaiFeed mNativeAD;
    private ZhaoCaiFeedListener mListener = new ZhaoCaiFeedListener() {
        @Override
        public void onFeedLoad(List<ZhaoCaiNative> list) {
            if (mConfig.platform != null
                    && list != null) {
                handleSuccess(mConfig.platform, list);
            }
        }

        @Override
        public void onFailed(int i, String s) {
            if (mConfig.platform != null) {
                handleFailure(mConfig.platform, i, s);
            }
        }
    };
    private WeakReference<Context> contextRef;

    private AdConfiguration getAdConfiguration(Context context, @NonNull ADOnlineConfig adOnlineConfig, int size) {
        if (adConfiguration == null) {
            ZhaoCaiSDK.INSTANCE.setAppId(context, adOnlineConfig.appKey);
        }
        return adConfiguration = new AdConfiguration.Builder()
                .setAdCount(size)
                .setCodeId(adOnlineConfig.subKey)
                .build();
    }

    @Override
    public void loadData(@Nullable Context context, int count) {
        if (context == null) {
            LogUtils.e("拉取广告被终止,当前上下文已被销毁");
            return;
        }
        if (TextUtils.isEmpty(mConfig.appKey) || TextUtils.isEmpty(mConfig.subKey) || TextUtils
                .isEmpty(mConfig.platform)) {
            handleFailure(mConfig.platform, -1, "appkey or subKey or platform is invalid");
            return;
        }
        try {
            if (contextRef != null) {
                Context lastContext = contextRef.get();
                //上下文切换后重新初始化
                if (lastContext == null || lastContext != context) {
                    LogUtils.i("wskj上下文变化,重新初始化");
                    mNativeAD = new ZhaoCaiFeed(context, getAdConfiguration(context, mConfig, count));
                    mNativeAD.addListener(mListener);
                }
            }
            contextRef = new WeakReference<>(context);
            if (mNativeAD == null) {
                LogUtils.i("wskj初始化广告");
                mNativeAD = new ZhaoCaiFeed(context, getAdConfiguration(context, mConfig, count));
                mNativeAD.addListener(mListener);
            }
            mNativeAD.loadAd();
            LogUtils.i("wskj拉取广告中......");
        } catch (Exception e) {
            mNativeAD = null;
            LogUtils.e("wskj拉取广告时出错{" + e.getLocalizedMessage() + "}");
        }
    }

    @Nullable
    @Override
    public Object getData(@Nullable Context context) {
        Lock lock = new ReentrantLock();
        lock.lock();
        if (context == null) {
            LogUtils.e("wskj取出广告被终止,当前上下文已被销毁");
            return null;
        }
        Object object = linkedQueue.poll();
        if (!isFast() && linkedQueue.size() < 3) {
            loadData(context, 3);
        }
        return object;
    }

    @Override
    public void onCleared() {
        super.onCleared();
        mNativeAD = null;
        contextRef = null;
    }
}
