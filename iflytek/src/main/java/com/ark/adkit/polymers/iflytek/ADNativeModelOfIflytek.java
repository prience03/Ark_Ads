package com.ark.adkit.polymers.iflytek;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.ark.adkit.basics.models.ADNativeModel;
import com.ark.adkit.basics.utils.LogUtils;
import com.iflytek.voiceads.*;

import java.lang.ref.WeakReference;
import java.util.List;

public class ADNativeModelOfIflytek extends ADNativeModel {

    private IFLYNativeAd mNativeAD;
    private IFLYNativeListener mListener = new IFLYNativeListener() {
        @Override
        public void onConfirm() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onADLoaded(List<NativeADDataRef> list) {
            if (list != null) {
                handleSuccess(mConfig.platform, list);
            }
        }

        @Override
        public void onAdFailed(AdError adError) {
            if (adError != null) {
                handleFailure(mConfig.platform, adError.getErrorCode(),
                        adError.getErrorDescription());
            }
        }
    };

    private WeakReference<Context> contextRef;

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
            if (contextRef != null) {
                Context lastContext = contextRef.get();
                //上下文切换后重新初始化
                if (lastContext == null || lastContext != context) {
                    LogUtils.i("上下文变化,重新初始化");
                    mNativeAD = new IFLYNativeAd(context, mConfig.subKey,
                            mListener);
                }
            }
            contextRef = new WeakReference<>(context);
            if (mNativeAD == null) {
                LogUtils.i("初始化广告");
                mNativeAD = new IFLYNativeAd(context, mConfig.subKey, mListener);
            }
            mNativeAD.setParameter(AdKeys.APPID, mConfig.appKey);
            mNativeAD.setParameter(AdKeys.DEBUG_MODE,
                    String.valueOf(false));
            mNativeAD.setParameter(AdKeys.DOWNLOAD_ALERT, String.valueOf(false));
            mNativeAD.loadAd(count);
            LogUtils.i("拉取广告中......");
        } catch (Exception e) {
            mNativeAD = null;
            LogUtils.e("拉取广告时出错{" + e.getLocalizedMessage() + "}");
        }
    }

    @Nullable
    @Override
    public Object getData(@Nullable final Context context) {
        if (context == null) {
            LogUtils.e("取出广告被终止,当前上下文已被销毁");
            return null;
        }
        Object object = linkedQueue.poll();
        if (!isFast() && linkedQueue.isEmpty()) {
            loadData(context, linkedQueue.isEmpty() ? 3 : 1);
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
