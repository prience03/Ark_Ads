package com.ark.adkit.polymers.ttad;

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
import com.bytedance.sdk.openadsdk.*;

import java.util.ArrayList;
import java.util.List;

public class ADMetaDataOfTT extends ADMetaData {

    private TTFeedAd mTTFeedAd;

    public ADMetaDataOfTT(@NonNull TTFeedAd ttFeedAd) {
        this.mTTFeedAd = ttFeedAd;
        mTTFeedAd.setVideoAdListener(new TTFeedAd.VideoAdListener() {
            @Override
            public void onVideoLoad(TTFeedAd ttFeedAd) {
                LogUtils.i("ttad video load");
            }

            @Override
            public void onVideoError(int i, int i1) {
                LogUtils.i("ttad video load error" + i + "," + i1);
            }

            @Override
            public void onVideoAdStartPlay(TTFeedAd ttFeedAd) {
                LogUtils.i("ttad video start play");
            }

            @Override
            public void onVideoAdPaused(TTFeedAd ttFeedAd) {
                LogUtils.i("ttad video pause play");

            }

            @Override
            public void onVideoAdContinuePlay(TTFeedAd ttFeedAd) {
                LogUtils.i("ttad video resume play");
            }
        });
    }

    @Override
    public boolean isVideo() {
        return mTTFeedAd.getImageMode() == TTAdConstant.IMAGE_MODE_VIDEO && getAdView() != null;
    }

    @Nullable
    @Override
    public View getAdView() {
        return mTTFeedAd.getAdView();
    }

    @NonNull
    @Override
    public Object getData() {
        return mTTFeedAd;
    }

    @NonNull
    @Override
    public String getTitle() {
        String title = mTTFeedAd.getTitle();
        if (!TextUtils.isEmpty(title)) {
            return title;
        }
        return "";
    }

    @NonNull
    @Override
    public String getSubTitle() {
        String subTitle = mTTFeedAd.getDescription();
        if (!TextUtils.isEmpty(subTitle)) {
            return subTitle;
        }
        return "";
    }

    @NonNull
    @Override
    public String getImgUrl() {
        if (getImgUrls().isEmpty()) {
            return "";
        }
        return getImgUrls().get(0);
    }

    @NonNull
    @Override
    public String getLogoUrl() {
        TTImage ttImage = mTTFeedAd.getIcon();
        if (ttImage != null) {
            String imgUrl = ttImage.getImageUrl();
            if (!TextUtils.isEmpty(imgUrl)) {
                return imgUrl;
            }
        }
        return getImgUrl();
    }

    @NonNull
    @Override
    public List<String> getImgUrls() {
        List<TTImage> list = mTTFeedAd.getImageList();
        List<String> mList = new ArrayList<>();
        if (list != null) {
            for (TTImage ttImage : list) {
                if (!TextUtils.isEmpty(ttImage.getImageUrl())) {
                    mList.add(ttImage.getImageUrl());
                }
            }
        }
        return mList;
    }

    @Override
    public boolean isApp() {
        return mTTFeedAd.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD;
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

                        mTTFeedAd.registerViewForInteraction(viewGroup, viewGroup,
                                new TTNativeAd.AdInteractionListener() {
                                    @Override
                                    public void onAdClicked(View view, TTNativeAd ttNativeAd) {
                                        LogUtils.i("ttad click" + ttNativeAd.getTitle());
                                    }

                                    @Override
                                    public void onAdCreativeClick(View view,
                                            TTNativeAd ttNativeAd) {

                                    }

                                    @Override
                                    public void onAdShow(TTNativeAd ttNativeAd) {
                                        LogUtils.i("ttad show" + ttNativeAd.getTitle());
                                    }
                                });

                    }
                });
    }

    @Override
    public void handleClick(@NonNull final ViewGroup viewGroup) {
        if (isVideo()) {
            DownloadStatusController dc = mTTFeedAd.getDownloadStatusController();
            if (dc != null) {
                dc.changeDownloadStatus();
            }
        }
    }

    @Override
    public void handleClick(@NonNull ViewGroup viewGroup, @Nullable View clickableView,
            int mDownX, int mDownY, int mUpX, int mUpY) {

    }

    @NonNull
    @Override
    public String getPlatform() {
        return ADPlatform.TTAD;
    }

    @NonNull
    @Override
    public String getPkgName() {
        return "";
    }
}
