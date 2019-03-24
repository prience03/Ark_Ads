package com.ark.adkit.polymers.zhaocai;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.LogUtils;
import com.zhaocai.ad.sdk.ZhaoCaiConstant;
import com.zhaocai.ad.sdk.ZhaoCaiImage;
import com.zhaocai.ad.sdk.ZhaoCaiNative;
import com.zhaocai.ad.sdk.ZhaoCaiNativeInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class ADMetaDataOfZhaoCai extends ADMetaData {

    private final ZhaoCaiNative mZhaoCaiNative;

    public ADMetaDataOfZhaoCai(@NonNull ZhaoCaiNative mZhaoCaiNative) {
        this.mZhaoCaiNative = mZhaoCaiNative;
    }

    @Nullable
    @Override
    public View getAdView() {
        return null;
    }

    @NonNull
    @Override
    public Object getData() {
        return mZhaoCaiNative;
    }

    @NonNull
    @Override
    public String getTitle() {
        String title = mZhaoCaiNative.getTitle();
        if (TextUtils.isEmpty(title)) {
            title = "";
        }
        return title;
    }

    @NonNull
    @Override
    public String getSubTitle() {
        String subTitle = mZhaoCaiNative.getDescription();
        if (TextUtils.isEmpty(subTitle)) {
            subTitle = "";
        }
        return subTitle;
    }

    @NonNull
    @Override
    public String getImgUrl() {
        List<ZhaoCaiImage> list = mZhaoCaiNative.getImageList();
        return list != null && list.size() > 0 ? list.get(0).getImageUrl() : "";
    }

    @NonNull
    @Override
    public String getLogoUrl() {
        String logoUrl = mZhaoCaiNative.getIcon().getImageUrl();
        return TextUtils.isEmpty(logoUrl) ? getImgUrl() : logoUrl;
    }

    @NonNull
    @Override
    public List<String> getImgUrls() {
        List<ZhaoCaiImage> list = mZhaoCaiNative.getImageList();
        List<String> mList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (ZhaoCaiImage image : list) {
                mList.add(image.getImageUrl());
            }
        }
        return mList;
    }

    @Override
    public boolean isApp() {
        return ZhaoCaiConstant.INTERACTION_TYPE_DOWNLOAD == mZhaoCaiNative.getInteractionType();
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
                        mZhaoCaiNative.registerViewForInteraction(viewGroup,
                                new ZhaoCaiNativeInteractionListener() {
                                    @Override
                                    public void onAdClicked(View view,
                                            ZhaoCaiNative zhaoCaiNative) {
                                        LogUtils.i("wskj click" + zhaoCaiNative.getTitle());
                                    }

                                    @Override
                                    public void onAdCreativeClick(View view,
                                            ZhaoCaiNative zhaoCaiNative) {

                                    }

                                    @Override
                                    public void onAdShow(ZhaoCaiNative zhaoCaiNative) {
                                        LogUtils.i("wskj show" + zhaoCaiNative.getTitle());
                                    }
                                });
                    }
                });
    }

    @Override
    public void handleClick(@NonNull ViewGroup viewGroup) {

    }

    @NonNull
    @Override
    public String getPlatform() {
        return ADPlatform.WSKJ;
    }

    @NonNull
    @Override
    public List<String> getAnalysisShowUrls() {
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> getAnalysisClickUrls() {
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> getAnalysisDownloadUrls() {
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> getAnalysisDownloadedUrls() {
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> getAnalysisInstallUrls() {
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> getAnalysisInstalledUrls() {
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public String getPkgName() {
        return "";
    }
}
