package com.ark.adkit.polymers.polymer.factory;


import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.models.ADSplashModel;

import java.util.HashMap;
import java.util.Map;

public class ADSplashFactory {

    private final static Map<String, ADSplashModel> sSplashMap = new HashMap<>();

    static {
        try {
            Class.forName("com.ark.adkit.polymers.gdt.ADSplashModelOfGdt");
            sSplashMap.put(ADPlatform.GDT, new com.ark.adkit.polymers.gdt.ADSplashModelOfGdt());
        } catch (ClassNotFoundException e) {
            //
        }
        sSplashMap.put(ADPlatform.YDT, new com.ark.adkit.polymers.ydt.ADSplashModelOfYdt());
        sSplashMap.put(ADPlatform.SELF, new com.ark.adkit.polymers.self.ADSplashModelOfSelf());
        try {
            Class.forName("com.ark.adkit.polymers.zhaocai.ADSplashModelOfZhaoCai");
            sSplashMap.put(ADPlatform.WSKJ,
                    new com.ark.adkit.polymers.zhaocai.ADSplashModelOfZhaoCai());
        } catch (ClassNotFoundException e) {
            //
        }

        try {
            Class.forName("com.ark.adkit.polymers.iflytek.ADSplashModelOfIflytek");
            sSplashMap.put(ADPlatform.IFLY,
                    new com.ark.adkit.polymers.iflytek.ADSplashModelOfIflytek());
        } catch (ClassNotFoundException e) {
            //
        }

        try {
            Class.forName("com.ark.adkit.polymers.longyun.ADSplashModelOfLongYun");
            sSplashMap.put(ADPlatform.LYJH,
                    new com.ark.adkit.polymers.longyun.ADSplashModelOfLongYun());
        } catch (ClassNotFoundException e) {
            //
        }
        try {
            Class.forName("com.ark.adkit.polymers.zhaocai.ADSplashModelOfZhaoCai");
            sSplashMap.put(ADPlatform.WSKJ,
                    new com.ark.adkit.polymers.zhaocai.ADSplashModelOfZhaoCai());
        } catch (ClassNotFoundException e) {
            //
        }
        try {
            Class.forName("com.ark.adkit.polymers.ttad.ADSplashModelOfTT");
            sSplashMap.put(ADPlatform.TTAD, new com.ark.adkit.polymers.ttad.ADSplashModelOfTT());
        } catch (ClassNotFoundException e) {
            //
        }
    }

    public static ADSplashModel createSplash(String sort) {
        return sSplashMap.get(sort);
    }
}
