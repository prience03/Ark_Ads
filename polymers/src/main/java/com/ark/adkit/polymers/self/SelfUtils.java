package com.ark.adkit.polymers.self;

import android.support.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class SelfUtils {

    private static Map<String, Data> dataMap = new Hashtable<>();

    private static class Data {
        int posType;
        SelfDataRef selfDataRef;
    }

    public static void recordInstall(int posType, SelfDataRef selfDataRef) {
        Data data = new Data();
        data.posType = posType;
        data.selfDataRef = selfDataRef;
        dataMap.put(selfDataRef.getPackageName(), data);
    }

    public static void anaInstall(String pkgName) {
        Data data = dataMap.get(pkgName);
        if (data != null) {
            data.selfDataRef.analysisInstall(data.posType);
        }
    }

    public static List<SelfDataRef> getNovaInfo(@NonNull String response) {
        List<SelfDataRef> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.optInt("code");
            if (code == 0) {
                JSONArray result = jsonObject.optJSONArray("res");
                if (result != null && result.length() > 0) {
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject adObj = result.optJSONObject(i);
                        if (adObj != null) {
                            SelfDataRef selfDataRef = new SelfDataRef();
                            selfDataRef.setPackageName(adObj.optString("package"));
                            selfDataRef.setAppRank(adObj.optInt("rank"));
                            selfDataRef.setId(adObj.optString("_id"));
                            selfDataRef.setWebADUrl(adObj.optString("webadurl"));
                            selfDataRef.setTargetUrl(adObj.optString("target"));
                            selfDataRef.setVerticalImage(adObj.optString("verticalimage"));
                            selfDataRef.setSplashImage(adObj.optString("splashimage"));
                            selfDataRef.setScheme(adObj.optString("scheme"));
                            selfDataRef.setType(adObj.optString("type"));
                            selfDataRef.setAppTitle(adObj.optString("title"));
                            selfDataRef.setAppDesc(adObj.optString("desc"));
                            selfDataRef.setAppLogo(adObj.optString("icon"));
                            selfDataRef.setAppImage(adObj.optString("horizontalimage"));
                            //cnt
                            JSONObject cntObj = adObj.optJSONObject("cnt");
                            selfDataRef.setIns(cntObj.optString("ins"));
                            selfDataRef.setViewCount(cntObj.optInt("view"));
                            selfDataRef.setClickCount(cntObj.optInt("clk"));

                            //下载时统计url
                            JSONArray downloadArray = adObj.optJSONArray("downloadurl");
                            ArrayList<String> downloadUrl = new ArrayList<>();
                            if (downloadArray != null && downloadArray.length() > 0) {
                                for (int j = 0; j < downloadArray.length(); j++) {
                                    downloadUrl.add(downloadArray.optString(j));
                                }
                            }
                            selfDataRef.setDownloadUrl(downloadUrl);
                            //展示时统计url
                            JSONArray viewArray = adObj.optJSONArray("viewurl");
                            ArrayList<String> viewUrl = new ArrayList<>();
                            if (viewArray != null && viewArray.length() > 0) {
                                for (int j = 0; j < viewArray.length(); j++) {
                                    viewUrl.add(viewArray.optString(j));
                                }
                            }
                            selfDataRef.setViewUrl(viewUrl);
                            //点击时统计url
                            JSONArray clickArray = adObj.optJSONArray("clickurl");
                            ArrayList<String> clickUrl = new ArrayList<>();
                            if (clickArray != null && clickArray.length() > 0) {
                                for (int j = 0; j < clickArray.length(); j++) {
                                    clickUrl.add(clickArray.optString(j));
                                }
                            }
                            selfDataRef.setClickUrl(clickUrl);
                            //安装时统计url
                            JSONArray installArray = adObj.optJSONArray("installurl");
                            ArrayList<String> installUrl = new ArrayList<>();
                            if (installArray != null && installArray.length() > 0) {
                                for (int j = 0; j < installArray.length(); j++) {
                                    installUrl.add(installArray.optString(j));
                                }
                            }
                            selfDataRef.setInstallUrl(installUrl);
                            //position
                            JSONArray posArray = adObj.optJSONArray("position");
                            ArrayList<String> posList = new ArrayList<>();
                            if (posArray != null && posArray.length() > 0) {
                                for (int j = 0; j < posArray.length(); j++) {
                                    posList.add(posArray.optString(j));
                                }
                            }
                            selfDataRef.setPositions(posList);
                            selfDataRef.setLocalViewAndClick();
                            list.add(selfDataRef);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
