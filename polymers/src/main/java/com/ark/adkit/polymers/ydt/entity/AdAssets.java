package com.ark.adkit.polymers.ydt.entity;

import com.google.gson.annotations.SerializedName;

public class AdAssets {
    /**
     * 素材地址
     */
    @SerializedName("url")
    public String url;
    /**
     * 素材内容的 md5 摘要，小写， 客户端可用其对素材
     */
    @SerializedName("digest")
    public String digest;
    /**
     * 素材类型 1-图片 2-gif 3-视频 4-音频
     */
    @SerializedName("materialType")
    public int materialType;
}
