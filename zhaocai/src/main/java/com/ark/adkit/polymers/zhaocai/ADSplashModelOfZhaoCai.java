package com.ark.adkit.polymers.zhaocai;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.ark.adkit.basics.configs.ADOnlineConfig;
import com.ark.adkit.basics.models.ADSplashModel;
import com.ark.adkit.basics.models.OnSplashListener;
import com.ark.adkit.basics.utils.LogUtils;
import com.zhaocai.ad.sdk.AdConfiguration;
import com.zhaocai.ad.sdk.ZhaoCaiSDK;
import com.zhaocai.ad.sdk.ZhaoCaiSplash;
import com.zhaocai.ad.sdk.ZhaoCaiSplashListener;

public class ADSplashModelOfZhaoCai extends ADSplashModel {

    private AdConfiguration adConfiguration;
    private ZhaoCaiSplash zhaoCaiSplash;

    private AdConfiguration getAdConfiguration(Context context, @NonNull ADOnlineConfig adOnlineConfig) {
        if (adConfiguration != null) {
            return adConfiguration;
        }
        ZhaoCaiSDK.INSTANCE.setAppId(context, adOnlineConfig.appKey);
        return adConfiguration = new AdConfiguration.Builder()
                .setAdCount(1)
                .setCodeId(adOnlineConfig.subKey)
                .build();
    }

    @Override
    protected void loadSplash(@NonNull final OnSplashListener onSplashListener) {
        final ViewGroup viewGroup = getValidViewGroup();
        final Activity activity = getValidActivity();
        if (activity == null || viewGroup == null) {
            LogUtils.e("splash is invalid");
            return;
        }
        if (mConfig == null) {
            onSplashListener.onAdFailed("wskj", -1, "splash config is null");
            return;
        }
        onSplashListener.onAdWillLoad(mConfig.platform);
        if (TextUtils.isEmpty(mConfig.appKey) || TextUtils.isEmpty(mConfig.subKey) || TextUtils
                .isEmpty(mConfig.platform)) {
            onSplashListener.onAdFailed(mConfig.platform, -1, "splash key is invalid");
            return;
        }
        if (adConfiguration == null) {
            adConfiguration = getAdConfiguration(activity, mConfig);
        }
        try {
            zhaoCaiSplash = new ZhaoCaiSplash(viewGroup, adConfiguration, 5000L);
            zhaoCaiSplash.addListener(new ZhaoCaiSplashListener() {
                @Override
                public void onDismissed() {
                    onSplashListener.onAdClosed(mConfig.platform);
                }

                @Override
                public void onADTick(int i) {
                    onSplashListener.onAdTimeTick(i);
                }

                @Override
                public void onFailed(int code, String message) {
                    onSplashListener.onAdFailed(mConfig.platform, -1, message);
                }

                @Override
                public void onAdShown() {
                    onSplashListener.onAdDisplay(mConfig.platform);
                }

                @Override
                public void onAdClick() {
                    onSplashListener.onAdClicked(mConfig.platform);
                }
            });
            zhaoCaiSplash.loadAd();
        } catch (Exception e) {
            onSplashListener.onAdFailed(mConfig.platform, -3, e.getMessage());
        }
    }

    @Override
    public void release() {
        super.release();
        zhaoCaiSplash = null;
        adConfiguration = null;
    }
}
