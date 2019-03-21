package com.ark.adkit.polymers.polymer.wrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.models.OnNativeListener;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.polymer.wiget.BannerAdView;
import com.ark.adkit.polymers.polymer.wiget.NativeAdView;
import com.ark.adkit.polymers.polymer.wiget.SmallNativeView;
import com.ark.adkit.polymers.polymer.wiget.VideoAdView;

/**
 * 原生广告包装类实现
 */
public class NativeWrapperImpl extends NativeWrapper {

    private static int mDownX, mDownY, mUpX, mUpY;


    /**
     * 加载横图广告
     *
     * @param context          上下文
     * @param viewGroup        广告容器
     * @param adMetaData       数据
     * @param onNativeListener 回调
     */
    @Override
    public void loadNativeView(@NonNull Context context, @NonNull final ViewGroup viewGroup,
                               @Nullable final ADMetaData adMetaData,
                               @Nullable OnNativeListener<ADMetaData> onNativeListener) {
        if (adMetaData == null) {
            if (onNativeListener != null) {
                onNativeListener.onFailure();
            }
            return;
        }
        NativeAdView adView = new NativeAdView(context);
        adView.setData(adMetaData);
        ViewGroup parent = (ViewGroup) adView.getParent();
        if (parent != null && parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        viewGroup.addView(adView, layoutParams);
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = (int) event.getX();
                        mDownY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mUpX = (int) event.getX();
                        mUpY = (int) event.getY();
                        v.performClick();
                        adMetaData.handleClick(viewGroup);
                        adMetaData.handleClick(viewGroup, v, mDownX, mDownY,
                                mUpX, mUpY);
                        break;
                }
                return true;
            }
        });
        if (onNativeListener != null) {
            onNativeListener.onSuccess(adMetaData);
        }
    }

    /**
     * 加载竖图广告
     *
     * @param context          上下文
     * @param viewGroup        广告容器
     * @param adMetaData       数据
     * @param onNativeListener 回调
     */
    @Override
    public void loadSmallNativeView(@NonNull Context context, @NonNull final ViewGroup viewGroup,
                                    @Nullable final ADMetaData adMetaData,
                                    @Nullable OnNativeListener<ADMetaData> onNativeListener) {
        if (adMetaData == null) {
            if (onNativeListener != null) {
                onNativeListener.onFailure();
            }
            return;
        }
        SmallNativeView adView = new SmallNativeView(context);
        adView.setData(adMetaData);
        ViewGroup parent = (ViewGroup) adView.getParent();
        if (parent != null && parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        viewGroup.addView(adView, layoutParams);
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = (int) event.getX();
                        mDownY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mUpX = (int) event.getX();
                        mUpY = (int) event.getY();
                        v.performClick();
                        adMetaData.handleClick(viewGroup);
                        adMetaData.handleClick(viewGroup, v, mDownX, mDownY,
                                mUpX, mUpY);
                        break;
                }
                return true;
            }
        });
        if (onNativeListener != null) {
            onNativeListener.onSuccess(adMetaData);
        }
    }

    /**
     * 加载视频广告，如果请求不到或者不包含视频则展示横图广告
     *
     * @param context          上下文
     * @param viewGroup        广告容器
     * @param adMetaData       数据
     * @param onNativeListener 回调
     */
    @Override
    public void loadVideoView(Context context, ViewGroup viewGroup, ADMetaData adMetaData,
                              OnNativeListener<ADMetaData> onNativeListener) {
        if (adMetaData == null) {
            if (ADTool.getADTool().isLoadOtherWhenVideoDisable()) {
                load(context, viewGroup, AD_COMMON, onNativeListener);
            } else if (onNativeListener != null) {
                onNativeListener.onFailure();
            }
            return;
        }
        if (adMetaData.isVideo()) {
            VideoAdView adView = new VideoAdView(context);
            adView.setData(adMetaData);
            ViewGroup parent = (ViewGroup) adView.getParent();
            if (parent != null && parent.getChildCount() > 0) {
                parent.removeAllViews();
            }
            if (viewGroup.getChildCount() > 0) {
                viewGroup.removeAllViews();
            }
            viewGroup.addView(adView);
            adView.refreshVideoView();
            if (onNativeListener != null) {
                onNativeListener.onSuccess(adMetaData);
            }
        } else {
            loadNativeView(context, viewGroup, adMetaData, onNativeListener);
        }
    }

    /**
     * 加载banner广告
     *
     * @param context          上下文
     * @param viewGroup        广告容器
     * @param adMetaData       数据
     * @param onNativeListener 回调
     */
    @Override
    public void loadBannerView(@NonNull Context context, @NonNull final ViewGroup viewGroup,
                               @Nullable final ADMetaData adMetaData,
                               @Nullable OnNativeListener<ADMetaData> onNativeListener) {
        if (adMetaData == null) {
            if (onNativeListener != null) {
                onNativeListener.onFailure();
            }
            return;
        }
        BannerAdView adView = new BannerAdView(context);
        adView.setData(adMetaData);
        ViewGroup parent = (ViewGroup) adView.getParent();
        if (parent != null && parent.getChildCount() > 0) {
            parent.removeAllViews();
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewGroup.getChildCount() > 0) {
            viewGroup.removeAllViews();
        }
        viewGroup.addView(adView, layoutParams);
        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mDownX = (int) event.getX();
                        mDownY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mUpX = (int) event.getX();
                        mUpY = (int) event.getY();
                        v.performClick();
                        adMetaData.handleClick(viewGroup);
                        adMetaData.handleClick(viewGroup, v, mDownX, mDownY,
                                mUpX, mUpY);
                        break;
                }
                return true;
            }
        });
        if (onNativeListener != null) {
            onNativeListener.onSuccess(adMetaData);
        }
    }
}
