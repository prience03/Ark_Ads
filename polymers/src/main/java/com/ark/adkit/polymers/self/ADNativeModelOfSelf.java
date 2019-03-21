package com.ark.adkit.polymers.self;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ark.adkit.basics.models.ADNativeModel;
import com.ark.adkit.basics.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class ADNativeModelOfSelf extends ADNativeModel {

    private SelfNativeAD mNativeAD;

    @Override
    public void loadData(@Nullable Context context, int count) {
        if (context == null) {
            return;
        }
        if (mNativeAD == null) {
            mNativeAD = new SelfNativeAD(context, SelfADStyle.INFO_LIST);
            mNativeAD.setListener(new SelfNativeAD.ADListener() {
                @Override
                public void onAdLoad(List<SelfDataRef> dataRefs) {
                    if (mConfig.platform != null
                            && dataRefs != null) {
                        List<SelfDataRef> list = new ArrayList<>();
                        for (SelfDataRef selfDataRef : dataRefs) {
                            if (!selfDataRef.isClickOver()
                                    && !selfDataRef.isInstalled()
                                    && !selfDataRef.isViewOver()) {
                                list.add(selfDataRef);
                            }
                        }
                        handleSuccess(mConfig.platform, list);
                    }
                }

                @Override
                public void onAdFailed(int errorCode, @NonNull String errorMsg) {
                    if (mConfig.platform != null) {
                        handleFailure(mConfig.platform, errorCode, errorMsg);
                    }
                }
            });
        }
        mNativeAD.loadAllADList();
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
            loadData(context, 1);
        }
        return object;
    }

    @Override
    public void onCleared() {
        super.onCleared();
        mNativeAD = null;
    }
}
