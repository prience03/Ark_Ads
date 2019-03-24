package com.ark.adkit.polymers.ydt;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.AppUtils;
import com.ark.adkit.polymers.ydt.entity.AdClick;
import com.ark.adkit.polymers.ydt.entity.AdData;
import com.ark.adkit.polymers.ydt.entity.AdDataRef;
import com.ark.adkit.polymers.ydt.utils.InstallUtils;
import com.ark.adkit.polymers.ydt.utils.YdtUtils;
import com.ark.dict.Utils;
import com.ark.uikit.webview.WebActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ADMetaDataOfYdt extends ADMetaData {

    private final AdDataRef adDataRef;
    private final YdtUtils ydtUtils;
    private int mDownX, mDownY, mUpX, mUpY;

    public ADMetaDataOfYdt(@NonNull AdDataRef adDataRef) {
        this.adDataRef = adDataRef;
        ydtUtils = new YdtUtils(Utils.getContext());
    }

    @Nullable
    @Override
    public View getAdView() {
        return null;
    }

    @NonNull
    @Override
    public Object getData() {
        return adDataRef;
    }

    @NonNull
    @Override
    public String getTitle() {
        String adTitle = "";
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            String title = adData.adTitle;
            if (!TextUtils.isEmpty(title)) {
                adTitle = title;
            }
        }
        return adTitle;
    }

    @NonNull
    @Override
    public String getSubTitle() {
        String adSubTitle = "";
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> descs = adData.descs;
            if (descs != null && !descs.isEmpty()) {
                adSubTitle = descs.get(0);
            }
        }
        return adSubTitle;
    }

    @NonNull
    @Override
    public String getImgUrl() {
        String imgUrl = "";
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> imgUrls = adData.imageUrl;
            if (imgUrls != null && !imgUrls.isEmpty()) {
                imgUrl = imgUrls.get(0);
            }
        }
        return imgUrl;
    }

    @NonNull
    @Override
    public String getLogoUrl() {
        String iconUrl = "";
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> iconUrls = adData.iconUrls;
            if (iconUrls != null && !iconUrls.isEmpty()) {
                iconUrl = iconUrls.get(0);
            }
        }
        return iconUrl;
    }

    @NonNull
    @Override
    public List<String> getImgUrls() {
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> imgUrls = adData.imageUrl;
            if (imgUrls != null && !imgUrls.isEmpty()) {
                return imgUrls;
            }
        }
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> getAnalysisShowUrls() {
        List<String> urls = new ArrayList<>();
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> list = adData.winNoticeUrls;
            if (list != null) {
                urls.addAll(list);
            }
        }
        return urls;
    }

    @NonNull
    @Override
    public List<String> getAnalysisClickUrls() {
        List<String> urls = new ArrayList<>();
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> list = adData.winCNoticeUrls;
            if (list != null) {
                urls.addAll(list);
            }
        }
        return urls;
    }

    @NonNull
    @Override
    public List<String> getAnalysisDownloadUrls() {
        List<String> urls = new ArrayList<>();
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> list = adData.arrDownloadTrackUrl;
            if (list != null) {
                urls.addAll(list);
            }
        }
        return urls;
    }

    @NonNull
    @Override
    public List<String> getAnalysisDownloadedUrls() {
        List<String> urls = new ArrayList<>();
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> list = adData.arrDownloadedTrakUrl;
            if (list != null) {
                urls.addAll(list);
            }
        }
        return urls;
    }

    @NonNull
    @Override
    public List<String> getAnalysisInstallUrls() {
        List<String> urls = new ArrayList<>();
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> list = adData.arrIntallTrackUrl;
            if (list != null) {
                urls.addAll(list);
            }
        }
        return urls;
    }

    @NonNull
    @Override
    public List<String> getAnalysisInstalledUrls() {
        List<String> urls = new ArrayList<>();
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            List<String> list = adData.arrIntalledTrackUrl;
            if (list != null) {
                urls.addAll(list);
            }
        }
        return urls;
    }

    @NonNull
    @Override
    public String getPkgName() {
        String pkg = "";
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            pkg = adData.packageName;
        }
        return pkg;
    }

    @Override
    public boolean isApp() {
        boolean isApp = false;
        List<AdData> dataList = adDataRef.metaGroup;
        if (dataList != null && !dataList.isEmpty()) {
            AdData adData = dataList.get(0);
            isApp = adData.interactionType == 2;
        }
        return isApp;
    }

    @NonNull
    @Override
    public String getPlatform() {
        return ADPlatform.YDT;
    }

    @Override
    public void handleView(@NonNull ViewGroup viewGroup) {
        ydtUtils.anaShow(this);
    }

    @Override
    public void handleClick(@NonNull ViewGroup viewGroup) {
        final String clickUrl = adDataRef.metaGroup.get(0).clickUrl;
        String strUrl = adDataRef.metaGroup.get(0).strLinkUrl;
        String url = strUrl == null ? clickUrl : strUrl;
        if (adDataRef.protocolType == 1) {
            url = url.replace("__DOWN_X__", mDownX + "")
                    .replace("__DOWN_Y__", mDownY + "")
                    .replace("__WIDTH__", viewGroup.getWidth() + "")
                    .replace("__HEIGHT__", viewGroup.getHeight() + "")
                    .replace("__UP_X__", mUpX + "")
                    .replace("__UP_Y__", mUpY + "");
        }
        String mUrl = url;
        if (adDataRef.metaGroup.get(0).interactionType == 1) {
            String deeplink = adDataRef.metaGroup.get(0).deepLink;
            if (!TextUtils.isEmpty(deeplink)) {
                mUrl = deeplink;
            }
        }
        if (!isApp()) {
            Log.e("logger", "start web:" + mUrl);
            WebActivity.launch(viewGroup.getContext(), mUrl);
            return;
        }
        doGet(Utils.getContext(), mUrl);
    }

    @Override
    public void setClickPosition(int mDownX, int mDownY, int mUpX, int mUpY) {
        super.setClickPosition(mDownX, mDownY, mUpX, mUpY);
        this.mDownX = mDownX;
        this.mDownY = mDownY;
        this.mUpX = mUpX;
        this.mUpY = mUpY;
    }

    @Override
    public void setClickView(@NonNull ViewGroup viewGroup, @Nullable View clickableView) {
        super.setClickView(viewGroup, clickableView);
    }

    private void doGet(@NonNull final Context mContext, String url) {
        AQuery aQuery = new AQuery(mContext);
        aQuery.ajax(url, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                if (object != null) {
                    final AdClick adClick = new AdClick();
                    try {
                        JSONObject jsonObject = new JSONObject(object);
                        adClick.ret = jsonObject.optInt("ret", -1);
                        JSONObject data = jsonObject.optJSONObject("data");
                        adClick.clickid = data.optString("clickid", "");
                        adClick.dstlink = data.optString("dstlink", "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (adClick.ret == 0) {
                        ydtUtils.anaClick(ADMetaDataOfYdt.this, adClick.clickid);
                        download(mContext, adClick);
                    } else {
                        Log.e("logger", "返回的格式错误:" + object);
                        Toast.makeText(mContext, "返回的格式错误", Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });
    }

    private void download(@NonNull final Context context, @NonNull final AdClick adClick) {
        AQuery aQuery = new AQuery(context);
        Toast.makeText(context, "开始下载" + getTitle(), Toast.LENGTH_SHORT).show();
        ydtUtils.anaStartDownload(ADMetaDataOfYdt.this, adClick.clickid);
        aQuery.download(adClick.dstlink,
                new File(Environment.getExternalStorageDirectory().getPath() + "/download/"
                        + adClick.clickid + ".apk"), new AjaxCallback<File>() {
                    @Override
                    public void callback(String url, File object, AjaxStatus status) {
                        super.callback(url, object, status);
                        if (object != null && object.exists()) {
                            ydtUtils.anaDownloadFinish(ADMetaDataOfYdt.this, adClick.clickid);
                            ydtUtils.anaStartInstall(ADMetaDataOfYdt.this, adClick.clickid);
                            InstallUtils.recordInstall(getPkgName(), getAnalysisInstalledUrls());
                            AppUtils.installApk(context, object);
                        } else {
                            Toast.makeText(context, "下载出错啦", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
