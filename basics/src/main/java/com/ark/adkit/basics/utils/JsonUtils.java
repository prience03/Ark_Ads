package com.ark.adkit.basics.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class JsonUtils {

    /**
     * 从Json字符串中获取Map
     *
     * @param jsonObjStr 字符串
     * @return Map
     */
    @NonNull
    public static Map<String, String> getKeyMap(@Nullable String jsonObjStr) {
        Map<String, String> map = new HashMap<>();
        if (jsonObjStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonObjStr);
                Iterator<String> iterator = jsonObject.keys();
                String key;
                String value;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    value = jsonObject.optString(key);
                    if (!TextUtils.isEmpty(value)) {
                        map.put(key, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 从Json字符串中获取集合
     *
     * @param jsonArrayStr 字符串
     * @return 集合
     */
    @NonNull
    public static List<String> getList(@Nullable String jsonArrayStr) {
        List<String> list = new ArrayList<>();
        if (jsonArrayStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonArrayStr);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String s = jsonArray.optString(i);
                    if (!TextUtils.isEmpty(s)) {
                        list.add(s);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 得到json文件中的内容
     *
     * @param context  上下文
     * @param fileName 文件名
     * @return String
     */
    @NonNull
    public static String getJson(@NonNull Context context, @NonNull String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return stringBuilder.toString();
    }
}
