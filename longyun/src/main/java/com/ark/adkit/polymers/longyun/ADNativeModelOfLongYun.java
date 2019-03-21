package com.ark.adkit.polymers.longyun;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.ark.adkit.basics.models.ADNativeModel;
import com.ark.adkit.basics.utils.LogUtils;
import com.longyun.juhe_sdk.interfaces.AdViewNativeListener;
import com.longyun.juhe_sdk.manager.AdViewNativeManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ADNativeModelOfLongYun extends ADNativeModel {

    private WeakReference<Context> contextRef;
    private AdViewNativeManager mNativeAD;

    private AdViewNativeListener mListener = new AdViewNativeListener() {
        @Override
        public void onAdFailed(String s) {
            if (mConfig.platform != null) {
                handleFailure(mConfig.platform, -1, s);
            }
        }

        @Override
        public void onAdRecieved(String s, ArrayList list) {
            if (mConfig.platform != null
                    && list != null) {
                handleSuccess(mConfig.platform, list);
            }
        }

        @Override
        public void onAdStatusChanged(String s, int i) {

        }
    };


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
            if (mNativeAD == null) {
                LongYunInit.init(context, mConfig);
            }
            if (contextRef != null) {
                Context lastContext = contextRef.get();
                //上下文切换后重新初始化
                if (lastContext == null || lastContext != context) {
                    LogUtils.i("lyjh上下文变化,重新初始化");
                    mNativeAD = AdViewNativeManager.getInstance(context);
                }
            }
            contextRef = new WeakReference<>(context);
            if (mNativeAD == null) {
                LogUtils.i("lyjh初始化广告");
                mNativeAD = AdViewNativeManager.getInstance(context);
            }
            mNativeAD.requestAd(context, mConfig.subKey, count, mListener);
            LogUtils.i("lyjh拉取广告中......");
        } catch (Exception e) {
            mNativeAD = null;
            LogUtils.e("lyjh拉取广告时出错{" + e.getLocalizedMessage() + "}");
        }
    }

    @Nullable
    @Override
    public Object getData(@Nullable final Context context) {
        if (context == null) {
            LogUtils.e("lyjh取出广告被终止,当前上下文已被销毁");
            return null;
        }
        Object object = linkedQueue.poll();
        if (!isFast() && linkedQueue.size() < 3) {
            loadData(context, 3 - linkedQueue.size());
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
