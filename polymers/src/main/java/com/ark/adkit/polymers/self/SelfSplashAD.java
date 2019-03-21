package com.ark.adkit.polymers.self;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.List;

public class SelfSplashAD {

    public void loadSplash(@NonNull final Context context, @NonNull final ViewGroup viewGroup,
            @NonNull final AdSplashListener listener) {
        SelfNativeAD selfNativeAD = new SelfNativeAD(context, SelfADStyle.SPLASH);
        selfNativeAD.setListener(new SelfNativeAD.ADListener() {
            @Override
            public void onAdLoad(List<SelfDataRef> dataRefs) {
                List<SelfDataRef> list = new ArrayList<>();
                for (SelfDataRef selfDataRef : dataRefs) {
                    if (!selfDataRef.isClickOver()
                            && !selfDataRef.isInstalled()
                            && !selfDataRef.isViewOver()) {
                        list.add(selfDataRef);
                    }
                }
                if (list.isEmpty()) {
                    listener.onAdFailed(-1, "没有获取到物料");
                } else {
                    final SelfDataRef selfDataRef = list.get(0);
                    final ADMetaDataOfSelf dataOfSelf = new ADMetaDataOfSelf(selfDataRef,
                            SelfADStyle.SPLASH);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    viewGroup.removeAllViews();
                    ImageView imageView = new ImageView(context);
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    viewGroup.addView(imageView, layoutParams);
                    AQuery aQuery = new AQuery(imageView);
                    aQuery.image(dataOfSelf.getImgUrl(), true, true);
                    dataOfSelf.handleView(viewGroup);
                    listener.onAdDisplay();
                    viewGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dataOfSelf.handleClick(viewGroup);
                            if (dataOfSelf.isApp()){
                                selfDataRef.analysisDownload(SelfADStyle.SPLASH);
                                selfDataRef.recordInstall(SelfADStyle.SPLASH);
                            }
                            listener.onAdClick();
                        }
                    });
                }
            }

            @Override
            public void onAdFailed(int errorCode, @NonNull String errorMsg) {
                listener.onAdFailed(errorCode, errorMsg);
            }
        });
        selfNativeAD.loadAllADList();
    }

    public interface AdSplashListener {

        void onAdClick();

        void onAdDisplay();

        void onAdFailed(int errorCode, @NonNull String errorMsg);
    }
}
