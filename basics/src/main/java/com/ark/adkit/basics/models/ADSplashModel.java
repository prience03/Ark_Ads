package com.ark.adkit.basics.models;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import com.ark.adkit.basics.configs.ADOnlineConfig;

import java.lang.ref.WeakReference;

public abstract class ADSplashModel {

    protected ADOnlineConfig mConfig;
    protected WeakReference<Activity> mActivityRef;
    protected WeakReference<ViewGroup> mViewGroupRef;

    public void initModel(@NonNull ADOnlineConfig aDOnlineConfig) {
        this.mConfig = aDOnlineConfig;
    }

    public final void loadSplashAD(@NonNull Activity activity,
            @NonNull ViewGroup adContainer,
            @NonNull OnSplashListener onSplashListener) {
        this.mActivityRef = new WeakReference<>(activity);
        this.mViewGroupRef = new WeakReference<>(adContainer);
        loadSplash(onSplashListener);
    }

    protected abstract void loadSplash(@NonNull OnSplashListener onSplashListener);

    public Activity getValidActivity() {
        if (mActivityRef != null) {
            Activity activity = mActivityRef.get();
            if (activity != null && !activity.isFinishing()) {
                return activity;
            }
        }
        return null;
    }

    public ViewGroup getValidViewGroup() {
        if (mViewGroupRef != null) {
            return mViewGroupRef.get();
        }
        return null;
    }

    public void release(){

    }
}

