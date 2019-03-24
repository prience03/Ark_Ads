package com.ark.adkit.basics.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import com.ark.adkit.basics.configs.ADStyle;

import java.util.ArrayList;
import java.util.List;

public abstract class ADMetaData {

    public boolean isVideo(){
        return false;
    }

    @Nullable
    public abstract View getAdView();

    @NonNull
    public abstract Object getData();

    @NonNull
    public abstract String getTitle();

    @NonNull
    public abstract String getSubTitle();

    @NonNull
    public abstract String getImgUrl();

    @NonNull
    public abstract String getLogoUrl();

    @NonNull
    public List<String> getImgUrls() {
        return new ArrayList<>();
    }

    public abstract boolean isApp();

    @NonNull
    public abstract String getPlatform();

    @NonNull
    public List<String> getAnalysisShowUrls() {
        return new ArrayList<>();
    }

    @NonNull
    public List<String> getAnalysisClickUrls() {
        return new ArrayList<>();
    }

    @NonNull
    public List<String> getAnalysisDownloadUrls() {
        return new ArrayList<>();
    }

    @NonNull
    public List<String> getAnalysisDownloadedUrls() {
        return new ArrayList<>();
    }

    @NonNull
    public List<String> getAnalysisInstallUrls() {
        return new ArrayList<>();
    }

    @NonNull
    public List<String> getAnalysisInstalledUrls() {
        return new ArrayList<>();
    }

    @NonNull
    public abstract String getPkgName();

    public abstract void handleView(@NonNull ViewGroup viewGroup);

    public abstract void handleClick(@NonNull ViewGroup viewGroup);

    public void setClickView(@NonNull ViewGroup viewGroup, @Nullable View clickableView) {
    }

    public void setClickPosition(int mDownX, int mDownY, int mUpX, int mUpY){

    }
}
