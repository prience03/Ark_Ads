package com.ark.adkit.basics.models;

import android.support.annotation.NonNull;

public interface OnSplashListener {

    void onAdWillLoad(@NonNull String platform);

    void onAdDisplay(@NonNull String platform, boolean hideSelfTicker);

    void onAdClicked(@NonNull String platform);

    void onAdFailed(@NonNull String platform, int errorCode, @NonNull String errorMsg);

    void onAdClosed(@NonNull String platform);

    void onAdTimeTick(long tick);

    void onAdShouldLaunch();
}
