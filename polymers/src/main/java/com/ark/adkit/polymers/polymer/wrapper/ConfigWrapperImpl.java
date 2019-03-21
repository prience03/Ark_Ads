package com.ark.adkit.polymers.polymer.wrapper;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.ark.adkit.basics.configs.ADStyle;
import com.ark.adkit.basics.utils.AppUtils;
import com.ark.adkit.basics.utils.JsonUtils;
import com.ark.adkit.polymers.polymer.ADTool;
import com.ark.adkit.polymers.polymer.constant.ADKey;
import com.ark.dict.ConfigMapLoader;
import com.ark.dict.Utils;
import org.json.JSONObject;

import java.util.*;

public class ConfigWrapperImpl implements ConfigWrapper {

    private ADKey mADKey;

    public ConfigWrapperImpl() {
        mADKey = new ADKey();
    }

    /**
     * 获取appKey
     *
     * @return appKey
     */
    @NonNull
    @Override
    public Map<String, String> getAppKeyMap() {
        return JsonUtils.getKeyMap(getConfig().get(mADKey.JsonKeyOfAppKey()));
    }

    /**
     * 获取subKey的map
     *
     * @param mStyle 开屏，列表，详情三个广告位
     * @return map
     */
    @NonNull
    @Override
    public Map<String, String> getSubKeyMap(@ADStyle int mStyle) {
        if (mStyle == ADStyle.POS_SPLASH) {
            return JsonUtils.getKeyMap(getConfig().get(mADKey.JsonKeyOfSplash()));
        } else if (mStyle == ADStyle.POS_STREAM_LIST) {
            return JsonUtils.getKeyMap(getConfig().get(mADKey.JsonKeyOfList()));
        } else if (mStyle == ADStyle.POS_STREAM_DETAIL) {
            return JsonUtils.getKeyMap(getConfig().get(mADKey.JsonKeyOfDetail()));
        } else if (mStyle == ADStyle.POS_STREAM_VIDEO) {
            return JsonUtils.getKeyMap(getConfig().get(mADKey.jsonKeyOfVideo()));
        }
        return new Hashtable<>();
    }

    /**
     * 获取config配置map
     *
     * @return map
     */
    @NonNull
    @Override
    public Map<String, String> getConfig() {
        String localConfig = ADTool.getADTool().getLocalConfig();
        if (!TextUtils.isEmpty(localConfig)) {
            try {
                return loadJsonToMap(localConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ConfigMapLoader.getInstance().getResponseMap();
    }

    @NonNull
    private Map<String, String> loadJsonToMap(String json) throws Exception {
        Map<String, String> mResponseMap = new HashMap<>();
        if (!TextUtils.isEmpty(json)) {
            JSONObject responseObject = new JSONObject(json);
            JSONObject resObject = responseObject.optJSONObject("res");
            JSONObject params = resObject.optJSONObject("params");
            JSONObject kvObj = params == null ? resObject : params;
            Iterator<String> iterator = kvObj.keys();
            String key;
            String value;
            while (iterator.hasNext()) {
                key = iterator.next();
                value = kvObj.optString(key);
                mResponseMap.put(key, value);
            }
        }
        return mResponseMap;
    }

    /**
     * 获取原生广告的排序
     *
     * @return list
     */
    @NonNull
    @Override
    public List<String> getNativeSort() {
        return JsonUtils.getList(getConfig().get(mADKey.JsonKeyOfNativeSort()));
    }

    /**
     * 获取开屏广告的排序
     *
     * @return list
     */
    @NonNull
    @Override
    public List<String> getSplashSort() {
        return JsonUtils.getList(getConfig().get(mADKey.JsonKeyOfSplashSort()));
    }

    /**
     * 获取视频广告的排序
     *
     * @return list
     */
    @NonNull
    @Override
    public List<String> getVideoSort() {
        return JsonUtils.getList(getConfig().get(mADKey.jsonKeyOfVideoSort()));
    }

    /**
     * 判断是否有广告，广告屏蔽逻辑
     *
     * @return 是否有广告
     */
    @Override
    public boolean hasAd() {
        Map<String, String> config = getConfig();
        boolean disableAll = TextUtils
                .equals(String.valueOf(true), config.get(mADKey.JsonKeyOfDisableAll()));
        List<String> channels = JsonUtils.getList(config.get(mADKey.JsonKeyOfDisableChannel()));
        String disableVersion = config.get(mADKey.JsonKeyOfDisableVersion());
        String currentVersion = AppUtils.getVersionName(Utils.getContext());
        String currentChannel = AppUtils.getChannel(Utils.getContext());
        if (disableAll) {
            return false;
        }
        if (disableVersion != null && TextUtils
                .equals(currentVersion, disableVersion.replaceAll("\"", ""))) {
            for (String s : channels) {
                if (TextUtils.equals(s.replaceAll("\"", ""), currentChannel)) {
                    return false;
                }
            }
        }
        return true;
    }

}
