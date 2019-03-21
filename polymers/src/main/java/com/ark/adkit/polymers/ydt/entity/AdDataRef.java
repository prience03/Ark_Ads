package com.ark.adkit.polymers.ydt.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdDataRef {

    @SerializedName("slotId")
    public String slotId;
    @SerializedName("htmlSnippet")
    public String htmlSnippet;
    @SerializedName("adKey")
    public String adKey;
    @SerializedName("tracks")
    public List<AdTrack> tracks;
    @SerializedName("metaGroup")
    public List<AdData> metaGroup;
    @SerializedName("adtext")
    public String adtext;
    @SerializedName("adlogo")
    public String adlogo;
    @SerializedName("adsimg")
    public String adsimg;
    @SerializedName("view_id")
    public String view_id;
    @SerializedName("protocolType")
    public int protocolType;
}
