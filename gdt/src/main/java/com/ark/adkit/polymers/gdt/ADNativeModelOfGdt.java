package com.ark.adkit.polymers.gdt;

import android.content.Context;
import android.support.annotation.Nullable;
import com.ark.adkit.basics.models.ADNativeModel;
import com.ark.adkit.basics.utils.LogUtils;
import com.qq.e.ads.cfg.BrowserType;
import com.qq.e.ads.cfg.DownAPPConfirmPolicy;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.comm.util.AdError;

import java.lang.ref.WeakReference;
import java.util.List;

public class ADNativeModelOfGdt extends ADNativeModel {

    private WeakReference<Context> contextRef;

    private NativeAD.NativeAdListener mListener = new NativeAD.NativeAdListener() {
        @Override
        public void onADLoaded(List<NativeADDataRef> list) {
            if (mConfig.platform != null && list != null) {
                handleSuccess(mConfig.platform, list);
            }
        }

        @Override
        public void onNoAD(AdError adError) {
            if (mConfig.platform != null
                    && adError != null) {
                handleFailure(mConfig.platform, adError.getErrorCode(),
                        adError.getErrorMsg());
            }
        }

        @Override
        public void onADStatusChanged(NativeADDataRef nativeADDataRef) {

        }

        @Override
        public void onADError(NativeADDataRef nativeADDataRef, AdError adError) {
            if (mConfig.platform != null
                    && adError != null) {
                handleFailure(mConfig.platform, adError.getErrorCode(),
                        adError.getErrorMsg());
            }
        }
    };
    private NativeAD mNativeAD;

    @Override
    public void loadData(@Nullable Context context, int count) {
        try {
            if (contextRef != null) {
                Context lastContext = contextRef.get();
                //上下文切换后重新初始化
                if (lastContext == null || lastContext != context) {
                    LogUtils.i("gdt上下文变化,重新初始化");
                    mNativeAD = new NativeAD(context, mConfig.appKey, mConfig.subKey, mListener);
                }
            }
            contextRef = new WeakReference<>(context);
            if (mNativeAD == null) {
                LogUtils.i("gdt初始化广告");
                mNativeAD = new NativeAD(context, mConfig.appKey, mConfig.subKey, mListener);
            }
            mNativeAD.setBrowserType(BrowserType.Inner);
            mNativeAD.setDownAPPConfirmPolicy(DownAPPConfirmPolicy.NOConfirm);
            mNativeAD.loadAD(count);
            LogUtils.i("gdt拉取广告中......");
        } catch (Exception e) {
            mNativeAD = null;
            LogUtils.e("gdt拉取广告时出错{" + e.getLocalizedMessage() + "}");
        }
    }

    @Nullable
    @Override
    public Object getData(@Nullable final Context context) {
        if (context == null) {
            LogUtils.e("gdt取出广告被终止,当前上下文已被销毁");
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
