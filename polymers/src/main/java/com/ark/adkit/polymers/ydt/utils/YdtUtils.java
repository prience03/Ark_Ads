package com.ark.adkit.polymers.ydt.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ark.adkit.polymers.ydt.ADMetaDataOfYdt;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class YdtUtils {

    private final Context mContext;

    public YdtUtils(Context context) {
        mContext = context;
    }

    public void anaShow(ADMetaDataOfYdt ydtData) {
        List<String> urls = ydtData.getAnalysisShowUrls();
        Log.i("logger", "曝光统计:start");
        for (String s : urls) {
            doGet(s, "曝光统计成功");
        }
    }

    public void anaClick(ADMetaDataOfYdt ydtData, String clickId) {
        List<String> urls = ydtData.getAnalysisClickUrls();
        Log.i("logger", "点击统计:start");
        for (String s : urls) {
            String url = s.replace("__CLICK_ID__", clickId + "");
            doGet(url, "点击统计成功");
        }
    }

    public void anaStartDownload(ADMetaDataOfYdt ydtData, String clickId) {
        List<String> urls = ydtData.getAnalysisDownloadUrls();
        Log.i("logger", "开始下载统计:start");
        for (String s : urls) {
            String url = s.replace("__CLICK_ID__", clickId + "");
            doGet(url, "开始下载统计成功");
        }
    }

    public void anaDownloadFinish(ADMetaDataOfYdt ydtData, String clickId) {
        List<String> urls = ydtData.getAnalysisDownloadedUrls();
        Log.i("logger", "下载完成统计:start");
        for (String s : urls) {
            String url = s.replace("__CLICK_ID__", clickId + "");
            doGet(url, "下载完成统计成功");
        }
    }

    public void anaStartInstall(ADMetaDataOfYdt ydtData, String clickId) {
        List<String> urls = ydtData.getAnalysisInstallUrls();
        Log.i("logger", "开始安装统计:start");
        for (String s : urls) {
            String url = s.replace("__CLICK_ID__", clickId + "");
            doGet(url, "开始安装统计成功");
        }
    }


    public void doGet(String url, final String msg) {
        AQuery aQuery = new AQuery(mContext);
        aQuery.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                Logger.e("logger", msg + "," + object);
            }
        });
    }

    @SuppressLint("MissingPermission")
    public String getBody(String channelId) {
        String ip = mContext.getSharedPreferences("ip", Context.MODE_PRIVATE)
                .getString("ip_address", DeviceUtils.getIpAddress());
        JSONObject jsonObject = new JSONObject();
        JSONObject deviceObj = new JSONObject();
        JSONObject networkObj = new JSONObject();
        String[] strings = LocationHelper.getLocation(mContext);
        try {
            deviceObj.put("androidId", DeviceUtils.getAndroidID(mContext));
            deviceObj.put("deviceType", 1);
            deviceObj.put("imei", DeviceUtils.getIMEI(mContext));
            deviceObj.put("mac", DeviceUtils.getMacAddress(mContext));
            deviceObj.put("model", DeviceUtils.getModel());
            deviceObj.put("osType", 1);
            deviceObj.put("osVersion", DeviceUtils.getSDKVersionName());
            deviceObj.put("screenHeight", DeviceUtils.getScreenHeight(mContext));
            deviceObj.put("screenWidth", DeviceUtils.getScreenWidth(mContext));
            deviceObj.put("vendor", DeviceUtils.getManufacturer());
            deviceObj.put("brand", DeviceUtils.getBrand());
            deviceObj.put("ua", System.getProperty("http.agent"));
            deviceObj.put("ppi", DeviceUtils.getScreenDensityDpi());
            deviceObj.put("screenOrientation ", 1);
            deviceObj.put("imsi", DeviceUtils.getIMSI(mContext));
            networkObj.put("connectionType", NetWorkUtils.getNetWorkStatus(mContext));
            networkObj.put("ip", ip);
            networkObj.put("operatorType", DeviceUtils.getSimOperator(mContext));
            networkObj.put("lon", strings[0]);
            networkObj.put("lat", strings[1]);
            jsonObject.put("device", deviceObj);
            jsonObject.put("network", networkObj);
            jsonObject.put("requestId", System.nanoTime());
            jsonObject.put("channelId", channelId);
        } catch (Exception e) {
            //
        }
        return jsonObject.toString();
    }
}
