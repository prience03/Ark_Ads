package com.ark.adkit.polymers.longyun;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.ark.adkit.basics.models.ADSplashModel;
import com.ark.adkit.basics.models.OnSplashListener;
import com.ark.adkit.basics.utils.LogUtils;
import com.longyun.juhe_sdk.interfaces.AdViewSplashListener;
import com.longyun.juhe_sdk.manager.AdViewSplashManager;

public class ADSplashModelOfLongYun extends ADSplashModel {

    private boolean firstFail = true;

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
        LongYunInit.init(activity, mConfig);
        firstFail = true;
        if (TextUtils.isEmpty(mConfig.appKey) || TextUtils.isEmpty(mConfig.subKey) || TextUtils
                .isEmpty(mConfig.platform)) {
            onSplashListener.onAdFailed(mConfig.platform, -1, "splash key is invalid");
            return;
        }
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AdViewSplashManager
                            .getInstance(activity).requestAd(activity, mConfig.subKey, viewGroup,
                            new AdViewSplashListener() {
                                @Override
                                public void onAdClick(String s) {
                                    onSplashListener.onAdClicked(mConfig.platform);
                                }

                                @Override
                                public void onAdDisplay(String s) {
                                    onSplashListener.onAdDisplay(mConfig.platform);
                                }

                                @Override
                                public void onAdClose(String s) {
                                    onSplashListener.onAdClosed(mConfig.platform);
                                }

                                @Override
                                public void onAdRecieved(String s) {
                                    if (getValidActivity() == null) {
                                        AdViewSplashManager
                                                .getInstance(activity)
                                                .timeoutReport(s);
                                    }
                                }

                                @Override
                                public void onAdFailed(String s) {
                                    if (firstFail) {
                                        firstFail = false;
                                        onSplashListener.onAdFailed(mConfig.platform, -1, s);
                                    }
                                }

                                @Override
                                public void onAdSplashNotifyCallback(String s, ViewGroup viewGroup,
                                                                     int i,
                                                                     int i1) {
                                    LogUtils.e("onAdSplashNotifyCallback" + s + "," + i + "," + i1);
                                }
                            });
                }
            });
        } catch (Exception e) {
            onSplashListener.onAdFailed(mConfig.platform, -3, e.getMessage());
        }
    }
}
