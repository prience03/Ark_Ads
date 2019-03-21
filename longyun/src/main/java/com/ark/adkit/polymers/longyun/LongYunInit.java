package com.ark.adkit.polymers.longyun;

import android.content.Context;
import android.text.TextUtils;
import com.ark.adkit.basics.configs.ADOnlineConfig;
import com.ark.adkit.basics.configs.ADStyle;
import com.ark.adkit.basics.utils.LogUtils;
import com.longyun.juhe_sdk.SDKConfiguration;
import com.longyun.juhe_sdk.manager.AdViewNativeManager;
import com.longyun.juhe_sdk.manager.AdViewSplashManager;

import java.util.HashMap;
import java.util.Map;

public class LongYunInit {

    private static Map<String, String> sMap = new HashMap<>();

    /**
     * 初始化开屏,原生广告
     * @param ctx Context
     * @param adOnlineConfig 广告配置
     */
    public static void init(Context ctx,ADOnlineConfig adOnlineConfig) {
        String appKey = adOnlineConfig.appKey;
        String subKey = adOnlineConfig.subKey;
        int adStyle = adOnlineConfig.adStyle;
        if (TextUtils.isEmpty(appKey) || TextUtils.isEmpty(subKey)) {
            return;
        }
        String value = sMap.get(subKey);
        if (TextUtils.equals(value, subKey)) {
            return;
        }
        try {
            SDKConfiguration sdkConfiguration = new SDKConfiguration.Builder(ctx)
                    .setAppKey(appKey)
                    .setInstlControlMode(SDKConfiguration.InstlControlMode.USERCONTROL)
                    .build();
            if (adStyle == ADStyle.POS_SPLASH) {
                AdViewSplashManager.getInstance(ctx).init(sdkConfiguration, subKey);
            } else if (adStyle == ADStyle.POS_STREAM_LIST || adStyle == ADStyle.POS_STREAM_DETAIL) {
                AdViewNativeManager.getInstance(ctx).init(sdkConfiguration, subKey);
            }
            sMap.put(subKey, subKey);
            LogUtils.i("龙云聚合初始化成功:appKey=" + appKey + ",subKey=" + subKey + ",style=" + adStyle);
        } catch (Exception e) {
            LogUtils.e("初始化龙云聚合失败:" + e.getMessage());
        }
    }
}
