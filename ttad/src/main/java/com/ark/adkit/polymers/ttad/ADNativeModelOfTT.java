package com.ark.adkit.polymers.ttad;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.ark.adkit.basics.configs.ADOnlineConfig;
import com.ark.adkit.basics.models.ADNativeModel;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.ttad.config.TTAdManagerHolder;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;

import java.lang.ref.WeakReference;
import java.util.List;

public class ADNativeModelOfTT extends ADNativeModel {

    private TTAdNative mTTAdNative;
    private TTAdNative.FeedAdListener listener = new TTAdNative.FeedAdListener() {
        @Override
        public void onError(int i, String s) {
            handleFailure(mConfig.platform, i, s);
        }

        @Override
        public void onFeedAdLoad(List<TTFeedAd> list) {
            if (list != null) {
                handleSuccess(mConfig.platform, list);
            }
        }
    };
    private AdSlot adSlot;

    @Override
    public void init(@Nullable ADOnlineConfig adOnlineConfig) {
        super.init(adOnlineConfig);
        if (adOnlineConfig != null) {
            adSlot = new AdSlot.Builder()
                    .setCodeId(adOnlineConfig.subKey)
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(690, 388)
                    .setAdCount(1)
                    .build();
        }
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
            if (mTTAdNative == null) {
                LogUtils.i("初始化广告");
                mTTAdNative = TTAdManagerHolder.getInstance(context, mConfig.appKey)
                        .createAdNative(context);
            }
            mTTAdNative.loadFeedAd(adSlot, listener);
            LogUtils.i("拉取广告中......");
        } catch (Exception e) {
            LogUtils.e("拉取广告时出错{" + e.getLocalizedMessage() + "}");
        }
    }

    @Nullable
    @Override
    public Object getData(@Nullable Context context) {
        if (context == null) {
            LogUtils.e("取出广告被终止,当前上下文已被销毁");
            return null;
        }
        Object object = linkedQueue.poll();
        if (!isFast() && linkedQueue.size() < 3) {
            loadData(context, linkedQueue.isEmpty() ? 3 : 1);
        }
        return object;
    }

    @Override
    public void onCleared() {
        super.onCleared();
        mTTAdNative = null;
    }
}
