package com.ark.adkit.basics.models;

import android.support.annotation.NonNull;

public abstract class OnSplashImpl implements OnSplashListener {

    public void onAdDisable(){

    }

    @Override
    public void onAdWillLoad(@NonNull String platform) {

    }

    @Override
    public void onAdDisplay(@NonNull String platform, boolean hideSelfTicker) {

    }

    @Override
    public void onAdClicked(@NonNull String platform) {

    }

    @Override
    public void onAdFailed(@NonNull String platform, int errorCode, @NonNull String errorMsg) {

    }

    @Override
    public void onAdClosed(@NonNull String platform) {

    }
}
