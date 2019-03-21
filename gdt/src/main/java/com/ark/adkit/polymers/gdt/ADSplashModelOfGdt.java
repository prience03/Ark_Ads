package com.ark.adkit.polymers.gdt;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.ark.adkit.basics.models.ADSplashModel;
import com.ark.adkit.basics.models.OnSplashListener;
import com.ark.adkit.basics.utils.LogUtils;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

public class ADSplashModelOfGdt extends ADSplashModel {

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
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new SplashAD(activity, viewGroup, mConfig.appKey, mConfig.subKey,
                            new SplashADListener() {
                                @Override
                                public void onADDismissed() {
                                    onSplashListener.onAdClosed(mConfig.platform);
                                }

                                @Override
                                public void onNoAD(AdError adError) {
                                    int code = 0;
                                    String msg = "onNoAD";
                                    if (adError != null) {
                                        code = adError.getErrorCode();
                                        msg = adError.getErrorMsg();
                                    }
                                    onSplashListener.onAdFailed(mConfig.platform, code, msg);
                                }

                                @Override
                                public void onADPresent() {
                                    onSplashListener.onAdDisplay(mConfig.platform, true);
                                }

                                @Override
                                public void onADClicked() {
                                    onSplashListener.onAdClicked(mConfig.platform);
                                }

                                @Override
                                public void onADTick(long l) {
                                    onSplashListener.onAdTimeTick(l / 1000);
                                }

                                //低版本的广点通没有此方法
                                public void onADExposure() {
                                    LogUtils.e("onADExposure");
                                }
                            });
                }
            });
        } catch (Exception e) {
            onSplashListener.onAdFailed(mConfig.platform, -1, e.getLocalizedMessage());
        }
    }

}
