package com.ark.adkit.polymers.iflytek;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.androidquery.AQuery;
import com.ark.adkit.basics.models.ADSplashModel;
import com.ark.adkit.basics.models.OnSplashListener;
import com.ark.adkit.basics.utils.LogUtils;
import com.iflytek.voiceads.*;

import java.util.List;

public class ADSplashModelOfIflytek extends ADSplashModel {

    @Override
    protected void loadSplash(@NonNull final OnSplashListener onSplashListener) {
        final ViewGroup viewGroup = getValidViewGroup();
        final Activity activity = getValidActivity();
        if (activity == null || viewGroup == null) {
            LogUtils.e("splash is invalid");
            return;
        }
        if (mConfig == null) {
            onSplashListener.onAdFailed("null", -1, "splash config is null");
            return;
        }
        onSplashListener.onAdWillLoad(mConfig.platform);
        if (TextUtils.isEmpty(mConfig.appKey) || TextUtils.isEmpty(mConfig.subKey) || TextUtils
                .isEmpty(mConfig.platform)) {
            onSplashListener.onAdFailed(mConfig.platform, -1, "splash key is invalid");
            return;
        }
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    IFLYNativeAd mNativeAD = new IFLYNativeAd(activity, mConfig.subKey,
                            new IFLYNativeListener() {
                                @Override
                                public void onADLoaded(List<NativeADDataRef> dataRefs) {
                                    if (dataRefs.isEmpty()) {
                                        onSplashListener
                                                .onAdFailed(mConfig.platform, -1, "没有获取到物料");
                                    } else {
                                        final ViewGroup viewGroup = getValidViewGroup();
                                        Activity activity = getValidActivity();
                                        if (activity == null || viewGroup == null) {
                                            LogUtils.e("splash is invalid");
                                            return;
                                        }
                                        final NativeADDataRef iflytekAD = dataRefs.get(0);
                                        final ADMetaDataOfIflytek metaDataOfIflytek = new ADMetaDataOfIflytek(
                                                iflytekAD);
                                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT);
                                        ImageView imageView = new ImageView(activity);
                                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                        viewGroup.addView(imageView, layoutParams);
                                        AQuery aQuery = new AQuery(imageView);
                                        aQuery.image(metaDataOfIflytek.getImgUrl(), true, true);
                                        onSplashListener.onAdDisplay(mConfig.platform);
                                        metaDataOfIflytek.handleView(viewGroup);
                                        viewGroup.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                metaDataOfIflytek.handleClick(viewGroup);
                                                onSplashListener.onAdClicked(mConfig.platform);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onAdFailed(AdError adError) {
                                    int code = 0;
                                    String msg = "onNoAD";
                                    if (adError != null) {
                                        code = adError.getErrorCode();
                                        msg = adError.getErrorDescription();
                                    }
                                    onSplashListener.onAdFailed(mConfig.platform, code, msg);
                                }

                                @Override
                                public void onConfirm() {

                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                    mNativeAD.setParameter(AdKeys.APPID, mConfig.appKey);
                    mNativeAD.setParameter(AdKeys.DEBUG_MODE,
                            String.valueOf(false));
                    mNativeAD.setParameter(AdKeys.DOWNLOAD_ALERT, String.valueOf(false));
                    mNativeAD.loadAd(1);
                }
            });
            LogUtils.i("拉取广告中......");
        } catch (Exception e) {
            LogUtils.e("拉取广告时出错{" + e.getLocalizedMessage() + "}");
        }
    }
}
