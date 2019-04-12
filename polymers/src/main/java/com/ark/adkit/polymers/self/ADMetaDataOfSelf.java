package com.ark.adkit.polymers.self;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ark.adkit.basics.configs.ADPlatform;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.utils.AppUtils;
import com.ark.adkit.basics.utils.FileUtils;
import com.ark.uikit.webview.WebActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ADMetaDataOfSelf extends ADMetaData {

    private final SelfDataRef mSelfDataRef;
    private final int mStyle;

    public ADMetaDataOfSelf(@NonNull SelfDataRef mSelfDataRef, @SelfADStyle int style) {
        this.mSelfDataRef = mSelfDataRef;
        this.mStyle = style;
    }

    @Nullable
    @Override
    public View getAdView() {
        return null;
    }

    @NonNull
    @Override
    public Object getData() {
        return mSelfDataRef;
    }

    @NonNull
    @Override
    public String getTitle() {
        return mSelfDataRef.getAppTitle();
    }

    @NonNull
    @Override
    public String getSubTitle() {
        return mSelfDataRef.getAppDesc();
    }

    @NonNull
    @Override
    public String getImgUrl() {
        return mStyle == SelfADStyle.SPLASH ? mSelfDataRef.getSplashImage()
                : mSelfDataRef.getAppImage();
    }

    public String getVerticalImage() {
        return mStyle == SelfADStyle.SPLASH ? mSelfDataRef.getSplashImage()
                : mSelfDataRef.getVerticalImage();
    }

    @NonNull
    @Override
    public String getLogoUrl() {
        return mSelfDataRef.getAppLogo();
    }

    @NonNull
    @Override
    public List<String> getImgUrls() {
        return new ArrayList<>();
    }

    @Override
    public boolean isApp() {
        return TextUtils.equals(mSelfDataRef.getType(), "android");
    }

    @Override
    public void handleView(@NonNull ViewGroup viewGroup) {
        mSelfDataRef.analysisView(mStyle);
    }

    @Override
    public void handleClick(@NonNull ViewGroup viewGroup) {
        mSelfDataRef.analysisClick(mStyle);
        mSelfDataRef.analysisDownload(mStyle);
        String url = mSelfDataRef.getTargetUrl();
        final Context context = viewGroup.getContext();
        if (isApp()) {
            if (AppUtils.checkApkExist(context, getPkgName())) {
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(getPkgName());
                try {
                    context.startActivity(intent);
                    return;
                } catch (Exception e) {
                    //
                }
            }
            File dir = FileUtils.getFileDir(context, "download");
            AQuery aQuery = new AQuery(context);
            Toast.makeText(context, "正在下载" + mSelfDataRef.getAppTitle(),
                    Toast.LENGTH_SHORT).show();
            aQuery.download(url, new File(dir, mSelfDataRef.getId() + ".apk"),
                    new AjaxCallback<File>() {

                        @Override
                        public void callback(String url, File object, AjaxStatus status) {
                            super.callback(url, object, status);
                            if (object != null && object.exists()) {
                                mSelfDataRef.recordInstall(mStyle);
                                AppUtils.installApk(context, object);
                            } else {
                                Toast.makeText(context, mSelfDataRef.getAppTitle() + "下载失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            WebActivity.launch(context, url);
        }
    }

    @NonNull
    @Override
    public String getPlatform() {
        return ADPlatform.SELF;
    }

    @NonNull
    @Override
    public List<String> getAnalysisShowUrls() {
        List<String> list = mSelfDataRef.getViewUrl();
        if (list != null) {
            return list;
        }
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> getAnalysisClickUrls() {
        List<String> list = mSelfDataRef.getClickUrl();
        if (list != null) {
            return list;
        }
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> getAnalysisDownloadedUrls() {
        List<String> list = mSelfDataRef.getDownloadUrl();
        if (list != null) {
            return list;
        }
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public List<String> getAnalysisInstalledUrls() {
        List<String> list = mSelfDataRef.getInstallUrl();
        if (list != null) {
            return list;
        }
        return new ArrayList<>();
    }

    @NonNull
    @Override
    public String getPkgName() {
        return mSelfDataRef.getPackageName();
    }
}
