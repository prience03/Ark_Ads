package com.ark.adkit.polymers.self;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import com.ark.adkit.basics.models.ADSplashModel;
import com.ark.adkit.basics.models.OnSplashListener;
import com.ark.adkit.basics.utils.LogUtils;

/**
 * 自营广告，用于原生 splash
 */
public class ADSplashModelOfSelf extends ADSplashModel {

    @Override
    public void loadSplash(@NonNull final OnSplashListener onSplashListener) {
        Activity activity = getValidActivity();
        ViewGroup viewGroup = getValidViewGroup();
        if (activity == null || viewGroup == null) {
            LogUtils.e("splash is invalid");
            return;
        }
        onSplashListener.onAdWillLoad(mConfig.platform);
        new SelfSplashAD().loadSplash(activity, viewGroup, new SelfSplashAD.AdSplashListener() {
            @Override
            public void onAdClick() {
                onSplashListener.onAdClicked(mConfig.platform);
                onSplashListener.onAdShouldLaunch();
            }

            @Override
            public void onAdDisplay() {
                onSplashListener.onAdDisplay(mConfig.platform, false);
            }

            @Override
            public void onAdFailed(int errorCode, @NonNull String errorMsg) {
                onSplashListener.onAdFailed(mConfig.platform, errorCode, errorMsg);
            }
        });
    }
}
