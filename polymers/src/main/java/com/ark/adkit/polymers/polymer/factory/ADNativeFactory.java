package com.ark.adkit.polymers.polymer.factory;


import android.support.annotation.Nullable;
import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.configs.ADStyle;
import com.ark.adkit.basics.models.ADNativeModel;

import java.util.HashMap;
import java.util.Map;

public class ADNativeFactory {

    private final static Map<String, ADNativeModel> sNativeMap = new HashMap<>();
    private final static Map<String, ADNativeModel> sDetailMap = new HashMap<>();
    private final static Map<String, ADNativeModel> sVideoMap = new HashMap<>();

    static {
        try {
            Class.forName("com.ark.adkit.polymers.gdt.ADNativeModelOfGdt");
            sNativeMap.put(ADPlatform.GDT, new com.ark.adkit.polymers.gdt.ADNativeModelOfGdt());
            sDetailMap.put(ADPlatform.GDT, new com.ark.adkit.polymers.gdt.ADNativeModelOfGdt());
        } catch (ClassNotFoundException e) {
            //
        }
        sNativeMap.put(ADPlatform.SELF, new com.ark.adkit.polymers.self.ADNativeModelOfSelf());
        sDetailMap.put(ADPlatform.SELF, new com.ark.adkit.polymers.self.ADNativeModelOfSelf());
        sNativeMap.put(ADPlatform.YDT, new com.ark.adkit.polymers.ydt.ADNativeModelOfYdt());
        sDetailMap.put(ADPlatform.YDT, new com.ark.adkit.polymers.ydt.ADNativeModelOfYdt());
        try {
            Class.forName("com.ark.adkit.polymers.zhaocai.ADNativeModelOfZhaoCai");
            sNativeMap.put(ADPlatform.WSKJ,
                    new com.ark.adkit.polymers.zhaocai.ADNativeModelOfZhaoCai());
            sDetailMap.put(ADPlatform.WSKJ,
                    new com.ark.adkit.polymers.zhaocai.ADNativeModelOfZhaoCai());
        } catch (ClassNotFoundException e) {
            //
        }
        try {
            Class.forName("com.ark.adkit.polymers.iflytek.ADNativeModelOfIflytek");
            sNativeMap.put(ADPlatform.IFLY,
                    new com.ark.adkit.polymers.iflytek.ADNativeModelOfIflytek());
            sDetailMap.put(ADPlatform.IFLY,
                    new com.ark.adkit.polymers.iflytek.ADNativeModelOfIflytek());
        } catch (ClassNotFoundException e) {
            //
        }
        try {
            Class.forName("com.ark.adkit.polymers.longyun.ADNativeModelOfLongYun");
            sNativeMap.put(ADPlatform.LYJH,
                    new com.ark.adkit.polymers.longyun.ADNativeModelOfLongYun());
            sDetailMap.put(ADPlatform.LYJH,
                    new com.ark.adkit.polymers.longyun.ADNativeModelOfLongYun());
        } catch (ClassNotFoundException e) {
            //
        }
        try {
            Class.forName("com.ark.adkit.polymers.zhaocai.ADNativeModelOfZhaoCai");
            sNativeMap.put(ADPlatform.TTAD,
                    new com.ark.adkit.polymers.zhaocai.ADNativeModelOfZhaoCai());
            sDetailMap.put(ADPlatform.TTAD,
                    new com.ark.adkit.polymers.zhaocai.ADNativeModelOfZhaoCai());
        } catch (ClassNotFoundException e) {
            //
        }
        try {
            Class.forName("com.ark.adkit.polymers.ttad.ADNativeModelOfTT");
            sNativeMap.put(ADPlatform.TTAD, new com.ark.adkit.polymers.ttad.ADNativeModelOfTT());
            sDetailMap.put(ADPlatform.TTAD, new com.ark.adkit.polymers.ttad.ADNativeModelOfTT());
            sVideoMap.put(ADPlatform.TTAD, new com.ark.adkit.polymers.ttad.ADNativeModelOfTT());
        } catch (ClassNotFoundException e) {
            //
        }
    }

    @Nullable
    public static ADNativeModel createNative(String sort, @ADStyle int adStyle) {
        if (adStyle == ADStyle.POS_STREAM_LIST) {
            return sNativeMap.get(sort);
        } else if (adStyle == ADStyle.POS_STREAM_DETAIL) {
            return sDetailMap.get(sort);
        } else if (adStyle == ADStyle.POS_STREAM_VIDEO) {
            return sVideoMap.get(sort);
        }
        return null;
    }
}
