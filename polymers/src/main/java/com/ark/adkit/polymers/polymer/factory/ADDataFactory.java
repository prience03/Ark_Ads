package com.ark.adkit.polymers.polymer.factory;

import android.text.TextUtils;
import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.configs.ADStyle;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.polymers.ydt.ADMetaDataOfYdt;

public class ADDataFactory {

    public static ADMetaData createData(String sort, Object o) {
        if (TextUtils.equals(sort, ADPlatform.GDT)) {
            return createGdtData(o);
        } else if (TextUtils.equals(sort, ADPlatform.IFLY)) {
            return createIflytekData(o);
        } else if (TextUtils.equals(sort, ADPlatform.SELF)) {
            return createSelfData(o);
        } else if (TextUtils.equals(sort, ADPlatform.LYJH)) {
            return createLongYunData(o);
        } else if (TextUtils.equals(sort, ADPlatform.WSKJ)) {
            return createZhaoCaiData(o);
        } else if (TextUtils.equals(sort, ADPlatform.TTAD)) {
            return createTtadData(o);
        } else if (TextUtils.equals(sort, ADPlatform.YDT)) {
            return createYdtData(o);
        }
        return null;
    }

    private static ADMetaDataOfYdt createYdtData(Object o) {
        if (o instanceof com.ark.adkit.polymers.ydt.entity.AdDataRef) {
            return new com.ark.adkit.polymers.ydt.ADMetaDataOfYdt(
                    (com.ark.adkit.polymers.ydt.entity.AdDataRef) o);
        }
        return null;
    }

    private static ADMetaData createGdtData(Object o) {
        try {
            Class.forName("com.qq.e.ads.nativ.NativeADDataRef");
            Class.forName("com.ark.adkit.polymers.gdt.ADMetaDataOfGdt");
            if (o instanceof com.qq.e.ads.nativ.NativeADDataRef) {
                return new com.ark.adkit.polymers.gdt.ADMetaDataOfGdt(
                        (com.qq.e.ads.nativ.NativeADDataRef) o);
            }
        } catch (ClassNotFoundException e) {
            //
        }
        return null;
    }

    private static ADMetaData createLongYunData(Object o) {
        try {
            Class.forName("com.longyun.juhe_sdk.model.natives.NativeAdModel");
            Class.forName("com.ark.adkit.polymers.longyun.ADMetaDataOfLongYun");
            if (o instanceof com.longyun.juhe_sdk.model.natives.NativeAdModel) {
                return new com.ark.adkit.polymers.longyun.ADMetaDataOfLongYun(
                        (com.longyun.juhe_sdk.model.natives.NativeAdModel) o);
            }
        } catch (ClassNotFoundException e) {
            //
        }
        return null;
    }

    private static ADMetaData createIflytekData(Object o) {
        try {
            Class.forName("com.ark.adkit.polymers.iflytek.ADMetaDataOfIflytek");
            Class.forName("com.iflytek.voiceads.NativeADDataRef");
            if (o instanceof com.iflytek.voiceads.NativeADDataRef) {
                return new com.ark.adkit.polymers.iflytek.ADMetaDataOfIflytek(
                        (com.iflytek.voiceads.NativeADDataRef) o);
            }
        } catch (ClassNotFoundException e) {
            //
        }
        return null;
    }

    private static ADMetaData createZhaoCaiData(Object o) {
        try {
            Class.forName("com.ark.adkit.polymers.zhaocai.ADMetaDataOfZhaoCai");
            Class.forName("com.zhaocai.ad.sdk.log.advanced.ZhaoCaiNativeAdvanced");
            if (o instanceof com.zhaocai.ad.sdk.ZhaoCaiNative) {
                return new com.ark.adkit.polymers.zhaocai.ADMetaDataOfZhaoCai(
                        (com.zhaocai.ad.sdk.log.advanced.ZhaoCaiNativeAdvanced) o);
            }
        } catch (ClassNotFoundException e) {
            //
        }
        return null;
    }

    private static ADMetaData createSelfData(Object o) {
        try {
            Class.forName("com.ark.adkit.polymers.self.ADMetaDataOfSelf");
            Class.forName("com.ark.adkit.polymers.self.SelfDataRef");
            if (o instanceof com.ark.adkit.polymers.self.SelfDataRef) {
                return new com.ark.adkit.polymers.self.ADMetaDataOfSelf(
                        (com.ark.adkit.polymers.self.SelfDataRef) o, ADStyle.POS_STREAM_LIST);
            }
        } catch (ClassNotFoundException e) {
            //
        }
        return null;
    }

    private static ADMetaData createTtadData(Object o) {
        try {
            Class.forName("com.ark.adkit.polymers.ttad.ADMetaDataOfTT");
            Class.forName("com.bytedance.sdk.openadsdk.TTFeedAd");
            if (o instanceof com.bytedance.sdk.openadsdk.TTFeedAd) {
                return new com.ark.adkit.polymers.ttad.ADMetaDataOfTT(
                        (com.bytedance.sdk.openadsdk.TTFeedAd) o);
            }
        } catch (ClassNotFoundException e) {
            //
        }
        return null;
    }
}
