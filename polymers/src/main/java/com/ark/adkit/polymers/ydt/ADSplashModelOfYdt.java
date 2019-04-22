package com.ark.adkit.polymers.ydt;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.ViewGroup;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.models.ADSplashModel;
import com.ark.adkit.basics.models.OnSplashListener;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.polymer.wiget.SplashAdView;
import com.ark.adkit.polymers.ydt.constant.UrlConst;
import com.ark.adkit.polymers.ydt.entity.AdDataRef;
import com.ark.adkit.polymers.ydt.entity.AdResp;
import com.ark.adkit.polymers.ydt.utils.DeviceUtils;
import com.ark.adkit.polymers.ydt.utils.Logger;
import com.ark.adkit.polymers.ydt.utils.YdtUtils;
import com.ark.dict.Utils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.ion.Ion;
import org.json.JSONObject;

public class ADSplashModelOfYdt extends ADSplashModel {

    private Context mContext;
    private SplashAdView splashAdView;
    private SplashAdView.SplashCallBack splashCallBack;
    private SharedPreferences sharedPreferences;
    private String ip;

    public ADSplashModelOfYdt() {
        this.mContext = Utils.getContext();
        sharedPreferences = mContext.getSharedPreferences("ip", Context.MODE_PRIVATE);
        ip = sharedPreferences.getString("ip_address", DeviceUtils.getIpAddress());
    }

    @NonNull
    private ADSplashModelOfYdt init(@NonNull ViewGroup viewGroup,
            @NonNull final SplashAdView.SplashCallBack splashCallBack) {
        this.splashCallBack = splashCallBack;
        splashAdView = new SplashAdView(viewGroup.getContext());
        viewGroup.addView(splashAdView);
        return this;
    }

    private void loadSplash() {
        if (TextUtils.isEmpty(ip) || ip.startsWith("192.168")) {
            loadIpAddress(true);
        } else {
            long time = sharedPreferences.getLong("ip_request_time", System.currentTimeMillis());
            Logger.w("logger", "curr:" + System.currentTimeMillis());
            Logger.w("logger", "time:" + time);
            if (System.currentTimeMillis() > 3600000 + time) {
                Logger.w("logger", "距离上次ip请求1个小时，刷新");
                loadIpAddress(false);
            }
            loadSplashAd();
        }
    }

    private void loadIpAddress(final boolean loadSplash) {
        AQuery aQuery = new AQuery(mContext);
        aQuery.ajax("http://pv.sohu.com/cityjson?ie=utf-8", String.class,
                new AjaxCallback<String>() {
                    @Override
                    public void callback(String url, String object, AjaxStatus status) {
                        super.callback(url, object, status);
                        try {
                            int start = object.indexOf("{");
                            int end = object.indexOf("}");
                            JSONObject jsonObject = new JSONObject(
                                    object.substring(start, end + 1));
                            ip = jsonObject.optString("cip", "");
                            Logger.i("logger", "ip load success:" + ip);
                            sharedPreferences.edit().putString("ip_address", ip)
                                    .putLong("ip_request_time", System.currentTimeMillis()).apply();
                        } catch (Exception e) {
                            Logger.e("logger", "ip load failed");
                        }
                        if (loadSplash) {
                            loadSplashAd();
                        }
                    }
                });
    }

    private void loadSplashAd() {
        Ion.with(mContext)
                .load(AsyncHttpPost.METHOD, UrlConst.URL)
                .setStringBody(new YdtUtils(mContext).getBody(mConfig.subKey))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        AdResp entity = null;
                        if (!TextUtils.isEmpty(result)) {
                            entity = AdResp.parseJson(result);
                        }
                        if (entity != null && entity.ads != null && !entity.ads.isEmpty()) {
                            if (splashCallBack != null) {
                                splashCallBack.onAdDisplay();
                            }
                            AdDataRef adData = entity.ads.get(0);
                            ADMetaDataOfYdt ydtData = new ADMetaDataOfYdt(adData);
                            splashAdView
                                    .setCallback(splashCallBack)
                                    .setData(ydtData);
                        } else {
                            if (splashCallBack != null) {
                                splashCallBack.onFailure(-1, "广告空");
                            }
                        }
                    }
                });
    }

    @Override
    protected void loadSplash(@NonNull final OnSplashListener onSplashListener) {
        final ViewGroup viewGroup = getValidViewGroup();
        final Activity activity = getValidActivity();
        if (activity == null || viewGroup == null) {
            LogUtils.e("splash is invalid");
            return;
        }
        if (mConfig == null) {
            onSplashListener.onAdFailed("null", -1, "splash config is null");
            return;
        }
        if (TextUtils.isEmpty(mConfig.subKey) || TextUtils
                .isEmpty(mConfig.platform)) {
            onSplashListener.onAdFailed("null", -1, "splash key is null");
            return;
        }
        onSplashListener.onAdWillLoad(mConfig.platform);
        init(viewGroup, new SplashAdView.SplashCallBack() {
            @Override
            public void onAdDisplay() {
                onSplashListener.onAdDisplay(ADPlatform.YDT);
            }

            @Override
            public void onFailure(int code, String msg) {
                onSplashListener.onAdFailed(ADPlatform.YDT, code, msg);
            }

            @Override
            public void onAdClick(boolean isAd, boolean isApp) {
                onSplashListener.onAdClicked("ydt");
                if (isAd && isApp) {
                    onSplashListener.onAdShouldLaunch();
                }
            }
        }).loadSplash();
    }
}
