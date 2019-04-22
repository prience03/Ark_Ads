package com.ark.adkit.polymers.ttad;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.ark.adkit.basics.models.ADSplashModel;
import com.ark.adkit.basics.models.OnSplashListener;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.ttad.config.TTAdManagerHolder;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;

public class ADSplashModelOfTT extends ADSplashModel {

    @Override
    protected void loadSplash(@NonNull final OnSplashListener onSplashListener) {
        final ViewGroup viewGroup = getValidViewGroup();
        final Activity activity = getValidActivity();
        if (activity == null || viewGroup == null) {
            LogUtils.e("splash is invalid");
            return;
        }
        if (mConfig == null) {
            onSplashListener.onAdFailed("null", -1, "splash config is null");
            return;
        }
        onSplashListener.onAdWillLoad(mConfig.platform);
        if (TextUtils.isEmpty(mConfig.appKey) || TextUtils.isEmpty(mConfig.subKey) || TextUtils
                .isEmpty(mConfig.platform)) {
            onSplashListener.onAdFailed(mConfig.platform, -1, "splash key is invalid");
            return;
        }
        try {
            final TTAdNative ttAdNative = TTAdManagerHolder.getInstance(activity, mConfig.appKey)
                    .createAdNative(activity);
            LogUtils.i("load splash,subKey:" + mConfig.subKey);
            AdSlot adSlot = new AdSlot.Builder()
                    .setCodeId(mConfig.subKey)
                    .setSupportDeepLink(true)
                    .setImageAcceptedSize(1080, 1920)
                    .build();
            ttAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
                @Override
                public void onError(int i, String s) {
                    onSplashListener.onAdFailed(mConfig.platform, i, s);
                }

                @Override
                public void onTimeout() {
                    onSplashListener.onAdFailed(mConfig.platform, -1, "广告超时");
                }

                @Override
                public void onSplashAdLoad(final TTSplashAd ttSplashAd) {
                    onSplashListener.onAdDisplay(mConfig.platform);
                    View splashAdView = ttSplashAd.getSplashView();
                    viewGroup.addView(splashAdView);
                    ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, int i) {
                            onSplashListener.onAdClicked(mConfig.platform);
                            if (ttSplashAd.getInteractionType()
                                    == TTAdConstant.FALLBACK_TYPE_DOWNLOAD) {
                                onSplashListener.onAdShouldLaunch();
                            }
                        }

                        @Override
                        public void onAdShow(View view, int i) {

                        }

                        @Override
                        public void onAdSkip() {
                            onSplashListener.onAdShouldLaunch();
                        }

                        @Override
                        public void onAdTimeOver() {
                            onSplashListener.onAdShouldLaunch();
                        }
                    });
                }
            }, 5000);
        } catch (Exception e) {
            onSplashListener.onAdFailed(mConfig.platform, -2, e.getLocalizedMessage());
        }
    }
}
