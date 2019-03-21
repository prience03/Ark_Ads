package com.ark.adkit.polymers.polymer.wrapper;

import android.support.annotation.NonNull;
import com.ark.adkit.basics.configs.ADStyle;

import java.util.List;
import java.util.Map;

public interface ConfigWrapper {

    /**
     * 获取appKey
     *
     * @return appKey
     */
    @NonNull
    Map<String, String> getAppKeyMap();

    /**
     * 获取subKey的map
     *
     * @param mStyle 开屏，列表，详情三个广告位
     * @return map
     */
    @NonNull
    Map<String, String> getSubKeyMap(@ADStyle int mStyle);

    /**
     * 获取config配置map
     *
     * @return map
     */
    @NonNull
    Map<String, String> getConfig();

    /**
     * 获取原生广告的排序
     *
     * @return list
     */
    @NonNull
    List<String> getNativeSort();

    /**
     * 获取开屏广告的排序
     *
     * @return list
     */
    @NonNull
    List<String> getSplashSort();

    /**
     * 获取视频广告的排序
     *
     * @return list
     */
    @NonNull
    List<String> getVideoSort();

    /**
     * 判断是否有广告，广告屏蔽逻辑
     *
     * @return 是否有广告
     */
    boolean hasAd();
}
