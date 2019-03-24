package com.ark.adkit.polymers.iflytek;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;
import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.AppUtils;
import com.iflytek.voiceads.NativeADDataRef;

import java.util.ArrayList;
import java.util.List;

public class ADMetaDataOfIflytek extends ADMetaData {

    private final NativeADDataRef mNativeADDataRef;
    private int retryCount;

    public ADMetaDataOfIflytek(@NonNull NativeADDataRef nativeADDataRef) {
        this.mNativeADDataRef = nativeADDataRef;
    }

    @NonNull
    @Override
    public Object getData() {
        return mNativeADDataRef;
    }

    @Nullable
    @Override
    public View getAdView() {
        return null;
    }

    @NonNull
    @Override
    public String getTitle() {
        String title = mNativeADDataRef.getTitle();
        if (TextUtils.isEmpty(title) || TextUtils.equals("null", title)) {
            title = "";
        }
        return title;
    }

    @NonNull
    @Override
    public String getSubTitle() {
        String subTitle = mNativeADDataRef.getSubTitle();
        if (TextUtils.isEmpty(subTitle) || TextUtils.equals("null", subTitle)) {
            subTitle = "";
        }
        return subTitle;
    }

    @NonNull
    @Override
    public String getImgUrl() {
        String imgUrl = mNativeADDataRef.getImage();
        if (TextUtils.isEmpty(imgUrl)) {
            imgUrl = "";
        }
        return imgUrl;
    }

    @NonNull
    @Override
    public String getLogoUrl() {
        String icon = mNativeADDataRef.getIcon();
        if (TextUtils.isEmpty(icon)) {
            icon = mNativeADDataRef.getImage();
        }
        return icon;
    }

    @NonNull
    @Override
    public List<String> getImgUrls() {
        List<String> imgUrls = new ArrayList<>();
        List<String> list = mNativeADDataRef.getImgUrls();
        if (list != null && !list.isEmpty()) {
            imgUrls.addAll(list);
        }
        return imgUrls;
    }

    @Override
    public boolean isApp() {
        return TextUtils.equals(com.iflytek.voiceads.NativeADDataRef.AD_DOWNLOAD,
                mNativeADDataRef.getAdtype());
    }

    @Override
    public void handleView(final @Nullable ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        viewGroup.setVisibility(View.VISIBLE);
        viewGroup.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        retryCount++;
                        if (mNativeADDataRef.isExposured() || retryCount > 3) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                viewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            } else {
                                viewGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            }
                        } else {
                            mNativeADDataRef.onExposured(viewGroup);
                        }
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
            if (isApp()) {
                Toast.makeText(viewGroup.getContext(), "开始下载", Toast.LENGTH_LONG).show();
            }
            mNativeADDataRef.onClicked(viewGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String getPlatform() {
        return ADPlatform.IFLY;
    }

    @NonNull
    @Override
    public String getPkgName() {
        return "";
    }
}
