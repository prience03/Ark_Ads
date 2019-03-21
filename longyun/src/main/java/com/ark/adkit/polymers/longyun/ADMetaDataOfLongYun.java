package com.ark.adkit.polymers.longyun;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.AppUtils;
import com.longyun.juhe_sdk.model.natives.NativeAdModel;

import java.util.ArrayList;
import java.util.List;

public class ADMetaDataOfLongYun extends ADMetaData {

    private NativeAdModel mNativeAdModel;

    public ADMetaDataOfLongYun(@NonNull NativeAdModel nativeAdModel) {
        this.mNativeAdModel = nativeAdModel;
    }

    @Nullable
    @Override
    public View getAdView() {
        return null;
    }

    @NonNull
    @Override
    public Object getData() {
        return mNativeAdModel;
    }

    @NonNull
    @Override
    public String getTitle() {
        String title = mNativeAdModel.getTitle();
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        return title;
    }

    @NonNull
    @Override
    public String getSubTitle() {
        String subTitle = mNativeAdModel.getDescription();
        if (TextUtils.isEmpty(subTitle)) {
            subTitle = "";
        }
        return subTitle;
    }

    @NonNull
    @Override
    public String getImgUrl() {
        String imgUrl = mNativeAdModel.getImageUrl();
        if (TextUtils.isEmpty(imgUrl)) {
            imgUrl = "";
        }
        return imgUrl;
    }

    @NonNull
    @Override
    public String getLogoUrl() {
        String imgUrl = mNativeAdModel.getImageUrl();
        if (TextUtils.isEmpty(imgUrl)) {
            imgUrl = "";
        }
        return imgUrl;
    }

    @NonNull
    @Override
    public List<String> getImgUrls() {
        return mNativeAdModel.getMultiPicUrls() == null ?
                new ArrayList<String>() : mNativeAdModel.getMultiPicUrls();
    }

    @Override
    public boolean isApp() {
        return mNativeAdModel.isApp();
    }

    @Override
    public void handleView(@NonNull final ViewGroup viewGroup) {
        viewGroup.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            viewGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        mNativeAdModel.onDisplay(viewGroup);
                    }
                });
    }

    @Override
    public void handleClick(@NonNull ViewGroup viewGroup) {
        if (isApp() && AppUtils.isTargetOver(viewGroup.getContext(), Build.VERSION_CODES.O)
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = viewGroup.getContext().getPackageManager()
                    .canRequestPackageInstalls();
            if (!hasInstallPermission) {
                AppUtils.startInstallPermissionSettingActivity(viewGroup.getContext());
                return;
            }
        }
        try {
            mNativeAdModel.onClick(viewGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String getPlatform() {
        return ADPlatform.LYJH;
    }

    @NonNull
    @Override
    public String getPkgName() {
        return "";
    }
}
