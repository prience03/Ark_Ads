package com.ark.adkit.polymers.ydt.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdData {

    @SerializedName("descs")
    public List<String> descs;
    @SerializedName("imageUrl")
    public List<String> imageUrl;
    @SerializedName("iconUrls")
    public List<String> iconUrls;
    @SerializedName("materialWidth")
    public int materialWidth;
    @SerializedName("materialHeight")
    public int materialHeight;
    @SerializedName("clickUrl")
    public String clickUrl;
    @SerializedName("strLinkUrl")
    public String strLinkUrl;
    @SerializedName("creativeType")
    public int creativeType;
    @SerializedName("interactionType")
    public int interactionType;
    @SerializedName("packageName")
    public String packageName;
    @SerializedName("appSize")
    public int appSize;
    @SerializedName("winNoticeUrls")
    public List<String> winNoticeUrls;
    @SerializedName("winCNoticeUrls")
    public List<String> winCNoticeUrls;
    @SerializedName("arrDownloadTrackUrl")
    public List<String> arrDownloadTrackUrl;
    @SerializedName("arrDownloadedTrakUrl")
    public List<String> arrDownloadedTrakUrl;
    @SerializedName("arrIntallTrackUrl")
    public List<String> arrIntallTrackUrl;
    @SerializedName("arrIntalledTrackUrl")
    public List<String> arrIntalledTrackUrl;
    @SerializedName("arrSkipTrackUrl")
    public List<String> arrSkipTrackUrl;
    @SerializedName("currentIndex")
    public int currentIndex;
    @SerializedName("brandName")
    public String brandName;
    @SerializedName("adTitle")
    public String adTitle;
    @SerializedName("downloadLink")
    public String downloadLink;
    @SerializedName("adControl")
    public AdControl adControl;
    @SerializedName("deepLink")
    public String deepLink;
    @SerializedName("adMark")
    public String adMark;
    @SerializedName("cacheAssets")
    public List<AdAssets> cacheAssets;
}
