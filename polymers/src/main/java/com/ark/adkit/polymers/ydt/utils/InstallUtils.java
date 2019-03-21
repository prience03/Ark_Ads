package com.ark.adkit.polymers.ydt.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class InstallUtils {

    private static Map<String, Data> dataMap = new Hashtable<>();

    public static class Data {
        public List<String> installUrls;
    }

    public static void recordInstall(String pkgName, List<String> installUrls) {
        if (TextUtils.isEmpty(pkgName)) {
            return;
        }
        Data data = new Data();
        data.installUrls = installUrls;
        dataMap.put(pkgName, data);
    }

    @Nullable
    public static Data getData(String pkgName) {
        return dataMap.get(pkgName);
    }
}
