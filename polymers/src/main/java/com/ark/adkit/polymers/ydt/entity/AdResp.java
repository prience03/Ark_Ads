package com.ark.adkit.polymers.ydt.entity;

import android.support.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AdResp {
    @SerializedName("ads")
    public List<AdDataRef> ads;
    @SerializedName("errorCode")
    public int errorCode;
    @SerializedName("fillType")
    public int fillType;
    @SerializedName("requestId")
    public String requestId;
    @SerializedName("slotType")
    public int slotType;
    @SerializedName("source")
    public int source;
    @SerializedName("type")
    public int type;

    @Nullable
    public static AdResp parseJson(String jsonObject) {
        AdResp adResp = null;
        try {
            Gson gson = new Gson();
            Type jsonType = new TypeToken<AdResp>() {
            }.getType();
            adResp = gson.fromJson(jsonObject, jsonType);
        } catch (Exception e) {
            //
        }
        return adResp;
    }
}
