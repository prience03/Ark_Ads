package com.ark.adkit.polymers.ttad.config;

import android.content.Context;
import com.ark.adkit.polymers.ttad.R;
import com.ark.adkit.polymers.ttad.service.AppDownloadStatusListener;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdManagerFactory;

/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class TTAdManagerHolder {

    private static boolean sInit;

    public static TTAdManager getInstance(Context context, String appKey) {
        TTAdManager ttAdManager = TTAdManagerFactory.getInstance(context);
        if (!sInit) {
            synchronized (TTAdManagerHolder.class) {
                if (!sInit) {
                    doInit(ttAdManager, context, appKey);
                    sInit = true;
                }
            }
        }
        return ttAdManager;
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(TTAdManager ttAdManager, Context context, String appKey) {
        ttAdManager.setAppId(appKey)
                .setAllowShowNotifiFromSDK(false)
                .setName(context.getResources().getString(R.string.app_name))
                .setGlobalAppDownloadListener(new AppDownloadStatusListener(context));
    }
}
