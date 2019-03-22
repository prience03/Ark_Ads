package com.ark.adkit.polymers.polymer.wrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import com.ark.adkit.basics.configs.ADConfig;
import com.ark.adkit.basics.configs.ADOnlineConfig;
import com.ark.adkit.basics.configs.ADStyle;
import com.ark.adkit.basics.configs.Strategy;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.models.ADNativeModel;
import com.ark.adkit.basics.models.OnNativeListener;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.polymer.factory.ADDataFactory;
import com.ark.adkit.polymers.polymer.factory.ADNativeFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class NativeWrapper {

    public static final int AD_COMMON = 0;//横图广告
    public static final int AD_SMALL = 1;//竖图广告
    public static final int AD_BANNER = 2;//banner广告
    public static final int AD_VIDEO = 3;//视频广告

    /**
     * 获取信息流广告配置
     *
     * @param adStyle 广告位
     * @return ADConfig
     */
    @NonNull
    public final ADConfig getConfig(@ADStyle int adStyle) {
        Map<String, String> appKeyMap = ADTool.getADTool().getManager()
                .getConfigWrapper()
                .getAppKeyMap();
        Map<String, String> subKeyMap = ADTool.getADTool().getManager()
                .getConfigWrapper()
                .getSubKeyMap(adStyle);
        List<String> pList;
        if (adStyle == AD_VIDEO) {
            pList = new ArrayList<>(ADTool.getADTool().getManager()
                    .getConfigWrapper()
                    .getVideoSort());
        } else {
            pList = new ArrayList<>(ADTool.getADTool().getManager()
                    .getConfigWrapper()
                    .getNativeSort());
        }
        boolean hasAd = ADTool.getADTool().getManager()
                .getConfigWrapper()
                .hasAd();
        return new ADConfig()
                .setHasAD(hasAd)
                .setPlatformList(pList)
                .setAppKey(appKeyMap)
                .setSubKey(subKeyMap);
    }

    /**
     * 获取列表广告位数据
     *
     * @param context 上下文
     * @return ADMetaData
     */
    @Nullable
    public ADMetaData getListNative(@NonNull Context context) {
        return getAllNative(context, ADStyle.POS_STREAM_LIST);
    }

    /**
     * 获取详情页广告位数据
     *
     * @param context 上下文
     * @return ADMetaData
     */
    @Nullable
    public ADMetaData getDetailNative(@NonNull Context context) {
        return getAllNative(context, ADStyle.POS_STREAM_DETAIL);
    }

    /**
     * 获取视频广告位数据
     *
     * @param context 上下文
     * @return ADMetaData
     */
    @Nullable
    public ADMetaData getVideoNative(@NonNull Context context) {
        return getAllNative(context, ADStyle.POS_STREAM_VIDEO);
    }

    /**
     * 获取广告位数据
     *
     * @param context 上下文
     * @param adStyle 广告位
     * @return ADMetaData
     */
    @Nullable
    private ADMetaData getAllNative(@NonNull Context context, @ADStyle int adStyle) {
        ADConfig mADConfig = getConfig(adStyle);
        List<String> sort = mADConfig.getSortList();
        if (!mADConfig.hasAD() || sort.isEmpty()) {
            LogUtils.e("广告已被禁用");
            return null;
        }
        ADMetaData metaData = null;
        int strategy = ADTool.getADTool().getStrategy();
        if (strategy == Strategy.shuffle) {
            Collections.shuffle(sort);
            LogUtils.i("随机顺序" + sort);
        } else if (strategy == Strategy.cycle) {
            if (ADTool.index-- <= Integer.MIN_VALUE) {
                ADTool.index = 0;
            }
            Collections.rotate(sort, ADTool.index);
            LogUtils.i("位移顺序" + sort);
        } else if (strategy == Strategy.order) {
            LogUtils.i("默认顺序" + sort);
        }
        for (int i = 0; i < sort.size(); i++) {
            String platform = sort.get(i);
            ADOnlineConfig adOnlineConfig = getConfig(mADConfig, adStyle, platform);
            metaData = getSingleNative(context, adStyle, platform, adOnlineConfig);
            if (metaData != null) {
                break;
            }
        }
        return metaData;
    }

    /**
     * 获取信息流广告id参数
     *
     * @param adStyle 广告位
     * @return ADConfig
     */
    private ADOnlineConfig getConfig(@NonNull ADConfig mADConfig, @ADStyle int adStyle,
            @NonNull String platform) {
        ADOnlineConfig adOnlineConfig = new ADOnlineConfig();
        adOnlineConfig.adStyle = adStyle;
        adOnlineConfig.platform = platform;
        adOnlineConfig.appKey = mADConfig.getAppKey(platform);
        adOnlineConfig.subKey = mADConfig.getSubKey(platform);
        return adOnlineConfig;
    }

    private ADMetaData getSingleNative(@NonNull Context context, @ADStyle int adStyle,
            @NonNull String platform, ADOnlineConfig adOnlineConfig) {
        ADMetaData mADMetaData = null;
        ADNativeModel adNativeModel = ADNativeFactory.createNative(platform, adStyle);
        if (adNativeModel != null) {
            adNativeModel.init(adOnlineConfig);
            Object object = adNativeModel.getData(context);
            if (object != null) {
                mADMetaData = ADDataFactory.createData(platform, object);
            }
        }
        return mADMetaData;
    }

    public <T> void load(@NonNull final Context context,
            @NonNull final ViewGroup viewGroup, final int type,
            OnNativeListener<T> onNativeListener) {
        ADMetaData nativeAD;
        if (type == AD_VIDEO) {
            nativeAD = getVideoNative(context);
        } else if (type == AD_BANNER) {
            nativeAD = getDetailNative(context);
        } else {
            nativeAD = getListNative(context);
        }
        switch (type) {
            case AD_COMMON:
                loadNativeView(context, viewGroup, nativeAD,
                        (OnNativeListener<ADMetaData>) onNativeListener);
                break;
            case AD_SMALL:
                loadSmallNativeView(context, viewGroup, nativeAD,
                        (OnNativeListener<ADMetaData>) onNativeListener);
                break;
            case AD_BANNER:
                loadBannerView(context, viewGroup, nativeAD,
                        (OnNativeListener<ADMetaData>) onNativeListener);
                break;
            case AD_VIDEO:
                loadVideoView(context, viewGroup, nativeAD,
                        (OnNativeListener<ADMetaData>) onNativeListener);
                break;
        }
    }

    public abstract void loadVideoView(Context context, ViewGroup viewGroup, ADMetaData videoData,
            OnNativeListener<ADMetaData> onNativeListener);

    public abstract void loadBannerView(Context context, ViewGroup viewGroup, ADMetaData nativeAD,
            OnNativeListener<ADMetaData> onNativeListener);

    public abstract void loadSmallNativeView(Context context, ViewGroup viewGroup,
            ADMetaData nativeAD, OnNativeListener<ADMetaData> onNativeListener);

    public abstract void loadNativeView(Context context, ViewGroup viewGroup, ADMetaData nativeAD,
            OnNativeListener<ADMetaData> onNativeListener);

    public final void loadVideoView(@NonNull Context context, @NonNull ViewGroup adContainer) {
        loadVideoView(context, adContainer, null);
    }

    public final void loadNativeView(@NonNull Context context, @NonNull ViewGroup adContainer) {
        loadNativeView(context, adContainer, null);
    }

    public final void loadSmallNativeView(@NonNull Context context,
            @NonNull ViewGroup adContainer) {
        loadSmallNativeView(context, adContainer, null);
    }

    public final void loadBannerView(@NonNull Context context, @NonNull ViewGroup adContainer) {
        loadBannerView(context, adContainer, null);
    }

    public final void loadVideoView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable OnNativeListener<ADMetaData> listener) {
        load(context, adContainer, AD_VIDEO, listener);
    }

    public final void loadNativeView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable OnNativeListener<ADMetaData> listener) {
        load(context, adContainer, AD_COMMON, listener);
    }

    public final void loadSmallNativeView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable OnNativeListener<ADMetaData> listener) {
        load(context, adContainer, AD_SMALL, listener);
    }

    public final void loadBannerView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable OnNativeListener<ADMetaData> listener) {
        load(context, adContainer, AD_BANNER, listener);
    }
}
