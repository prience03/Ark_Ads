package com.ark.adkit.polymers.gdt;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.AppUtils;
import com.qq.e.ads.nativ.NativeADDataRef;

import java.util.ArrayList;
import java.util.List;

public class ADMetaDataOfGdt extends ADMetaData {

    private final NativeADDataRef mNativeADDataRef;

    public ADMetaDataOfGdt(@NonNull NativeADDataRef nativeADDataRef) {
        mNativeADDataRef = nativeADDataRef;
    }

    @Nullable
    @Override
    public View getAdView() {
        return null;
    }

    @NonNull
    @Override
    public Object getData() {
        return mNativeADDataRef;
    }

    @NonNull
    @Override
    public String getTitle() {
        String title = mNativeADDataRef.getTitle();
        return title == null ? "" : title;
    }

    @NonNull
    @Override
    public String getSubTitle() {
        String subTitle = mNativeADDataRef.getDesc();
        return subTitle == null ? "" : subTitle;
    }

    @NonNull
    @Override
    public String getImgUrl() {
        String imgUrl = mNativeADDataRef.getImgUrl();
        return imgUrl == null ? "" : imgUrl;
    }

    @NonNull
    @Override
    public String getLogoUrl() {
        String logoUrl = mNativeADDataRef.getIconUrl();
        return logoUrl == null ? "" : logoUrl;
    }

    @NonNull
    @Override
    public List<String> getImgUrls() {
        List<String> list = mNativeADDataRef.getImgList();
        return list == null ? new ArrayList<String>() : list;
    }

    @Override
    public boolean isApp() {
        return mNativeADDataRef.isAPP();
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
        mNativeADDataRef.onClicked(viewGroup);
    }

    @Override
    public void handleView(@NonNull ViewGroup viewGroup) {
        mNativeADDataRef.onExposured(viewGroup);
    }

    @NonNull
    @Override
    public String getPlatform() {
        return ADPlatform.GDT;
    }

    @NonNull
    @Override
    public String getPkgName() {
        return "";
    }
}

