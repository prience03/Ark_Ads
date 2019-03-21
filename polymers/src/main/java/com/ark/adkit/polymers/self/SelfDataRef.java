package com.ark.adkit.polymers.self;

import android.text.TextUtils;
import com.ark.adkit.basics.utils.AppUtils;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.dict.ArkKv;
import com.ark.dict.ConfigMapLoader;
import com.ark.dict.Utils;
import com.ark.net.urlconn.StringCallback;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.simple.SimpleResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class SelfDataRef implements Serializable {

    private static final long serialVersionUID = 4179639574492937543L;
    private static final String AND_POS_SPLASH = "&position=splash";
    private static final String AND_POS_NATIVE = "&position=infolist";
    private static final String AND_POS_INTER_RECOMMEND = "&position=interrecommend";
    private ArrayList<String> viewUrl;
    private ArrayList<String> installUrl;
    private ArrayList<String> clickUrl;
    private ArrayList<String> positions;
    private ArrayList<String> downloadUrl;
    private String targetUrl;
    private String webADUrl;
    private int appRank;
    private String packageName;
    private String scheme;
    private String type;
    private String id;
    private String ins;
    private int viewCount;
    private int clickCount;
    private String splashImage;
    private String verticalImage;
    private String appTitle;
    private String appDesc;
    private String appLogo;
    private String appImage;

    private int localViewCount;
    private int localClickCount;

    public ArrayList<String> getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(ArrayList<String> viewUrl) {
        this.viewUrl = viewUrl;
    }

    public ArrayList<String> getInstallUrl() {
        return installUrl == null ? new ArrayList<String>() : installUrl;
    }

    public void setInstallUrl(ArrayList<String> installUrl) {
        this.installUrl = installUrl;
    }

    public ArrayList<String> getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(ArrayList<String> clickUrl) {
        this.clickUrl = clickUrl;
    }

    public ArrayList<String> getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(ArrayList<String> downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getWebADUrl() {
        return webADUrl;
    }

    public void setWebADUrl(String webADUrl) {
        this.webADUrl = webADUrl;
    }

    public int getAppRank() {
        return appRank;
    }

    public void setAppRank(int appRank) {
        this.appRank = appRank;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<String> positions) {
        this.positions = positions;
    }

    public String getIns() {
        return ins;
    }

    public void setIns(String ins) {
        this.ins = ins;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getSplashImage() {
        return splashImage;
    }

    public void setSplashImage(String splashImage) {
        this.splashImage = splashImage;
    }

    public String getVerticalImage() {
        return verticalImage;
    }

    public void setVerticalImage(String verticalImage) {
        this.verticalImage = verticalImage;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(String appLogo) {
        this.appLogo = appLogo;
    }

    public String getAppImage() {
        return appImage;
    }

    public void setAppImage(String appImage) {
        this.appImage = appImage;
    }

    public boolean isSplash() {
        return positions != null && positions.contains("splash");
    }

    public boolean isNative() {
        return positions != null && positions.contains("infolist");
    }

    public boolean isRecommend() {
        return positions != null && positions.contains("interrecommend");
    }

    public void setLocalViewAndClick() {
        this.localViewCount = getInt(getViewSPKey(), 0);
        this.localClickCount = getInt(getClickSPKey(), 0);
    }

    private void saveInt(String key, int value) {
        ArkKv.saveInt(key, value);
    }

    private int getInt(String key, int defValue) {
        return ArkKv.getInt(key, defValue);
    }

    /**
     * 点击此处是否溢出，第二天重新计算
     *
     * @return if true 溢出
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isClickOver() {
        return localClickCount >= getClickCount();
    }

    /**
     * 展示次数是否溢出,第二天重新计算
     *
     * @return if true 溢出
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isViewOver() {
        return localViewCount >= getViewCount();
    }

    /**
     * 是否已安装
     *
     * @return if true 已安装
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInstalled() {
        return AppUtils.checkApkExist(Utils.getContext(), getPackageName());
    }


    public void analysisView(int posType) {
        saveInt(getViewSPKey(), ++localViewCount);
        String pos = "";
        pos = getPos(posType, pos);
        LogUtils.i("analysis view,pos:" + pos);
        for (String s : getViewUrl()) {
            analysis(s, pos);
        }
    }

    private String getPos(int posType, String pos) {
        switch (posType) {
            case SelfADStyle.SPLASH:
                pos = AND_POS_SPLASH;
                break;
            case SelfADStyle.INFO_LIST:
                pos = AND_POS_NATIVE;
                break;
            case SelfADStyle.INTER_RECOMMEND:
                pos = AND_POS_INTER_RECOMMEND;
                break;
        }
        return pos;
    }

    public void analysisClick(int posType) {
        saveInt(getClickSPKey(), ++localClickCount);
        String pos = "";
        pos = getPos(posType, pos);
        LogUtils.i("analysis click,pos:" + pos);
        for (String s : getClickUrl()) {
            analysis(s, pos);
        }
    }

    public void analysisInstall(int posType) {
        String pos = "";
        pos = getPos(posType, pos);
        LogUtils.i("analysis install,pos:" + pos);
        for (String s : getInstallUrl()) {
            analysis(s, pos);
        }
    }

    public void recordInstall(int posType) {
        SelfUtils.recordInstall(posType, this);
    }

    public void analysisDownload(int posType) {
        String pos = "";
        pos = getPos(posType, pos);
        LogUtils.i("analysis download,pos:" + pos);
        for (String s : getDownloadUrl()) {
            analysis(s, pos);
        }
    }

    /**
     * 统计请求
     */
    private void analysis(final String url, final String pos) {
        if (!TextUtils.isEmpty(url)) {
            Kalle.get(url)
                    .perform(new StringCallback<String>(Utils.getContext()) {
                        @Override
                        public void onResponse(SimpleResponse<String, String> response) {
                            if (response.isSucceed()) {
                                LogUtils.i("自营广告统计成功: " + response.succeed());
                            } else {
                                LogUtils.w("自营广告统计失败，请检查: " + response.failed());
                            }
                        }
                    });
        }
    }

    private String getClickSPKey() {
        return "key_clickTimes_" + getId() + "_" + DateUtils.getCurDateStr(DateUtils.FORMAT_YMD);
    }

    private String getViewSPKey() {
        return "key_viewTimes_" + getId() + "_" + DateUtils.getCurDateStr(DateUtils.FORMAT_YMD);
    }

    @Override
    public String toString() {
        return "NovaInfo{" +
                "viewUrl=" + viewUrl +
                ", installUrl=" + installUrl +
                ", clickUrl=" + clickUrl +
                ", positions=" + positions +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", webADUrl='" + webADUrl + '\'' +
                ", appRank=" + appRank +
                ", packageName='" + packageName + '\'' +
                ", scheme='" + scheme + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", ins='" + ins + '\'' +
                ", viewCount=" + viewCount +
                ", clickCount=" + clickCount +
                ", splashImage='" + splashImage + '\'' +
                ", verticalImage='" + verticalImage + '\'' +
                ", appTitle='" + appTitle + '\'' +
                ", appDesc='" + appDesc + '\'' +
                ", appLogo='" + appLogo + '\'' +
                ", appImage='" + appImage + '\'' +
                ", localViewCount=" + localViewCount +
                ", localClickCount=" + localClickCount +
                '}';
    }
}
