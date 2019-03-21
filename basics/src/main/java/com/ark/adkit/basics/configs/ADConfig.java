package com.ark.adkit.basics.configs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ADConfig {

    private List<String> sortList = new ArrayList<>();
    private Hashtable<String, String> appKeyMap = new Hashtable<>();
    private Hashtable<String, String> subKeyMap = new Hashtable<>();
    private boolean has;

    public boolean hasAD() {
        return has;
    }

    public ADConfig setHasAD(boolean has) {
        this.has = has;
        return this;
    }

    public int size() {
        return sortList.size();
    }

    @NonNull
    public List<String> getSortList() {
        return sortList;
    }

    public ADConfig setAppKey(@Nullable Map<String, String> map) {
        if (map != null) {
            for (String s : map.keySet()) {
                String value = map.get(s);
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(value)) {
                    appKeyMap.put(s, map.get(s));
                }
            }
        }
        return this;
    }

    @Nullable
    public String getAppKey(String platform) {
        return appKeyMap.get(platform);
    }

    @Nullable
    public String getSubKey(String platform) {
        return subKeyMap.get(platform);
    }

    @NonNull
    public ADConfig setSubKey(Map<String, String> map) {
        for (String s : map.keySet()) {
            String value = map.get(s);
            if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(value)) {
                subKeyMap.put(s, map.get(s));
            }
        }
        return this;
    }

    public ADConfig setPlatformList(@Nullable List<String> platformList) {
        sortList.clear();
        if (platformList != null) {
            sortList.addAll(platformList);
        }
        return this;
    }
}

