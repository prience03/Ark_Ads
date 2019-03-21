package com.ark.adkit.polymers.ydt.entity;

import com.google.gson.annotations.SerializedName;

public class AdControl {
    /**
     * 广告时长，从广告渲染成功并对 用户可见起算，单位：毫秒
     */
    @SerializedName("duration")
    public long duration;
    /**
     * 仅离线广告有效，广告有效性的 起始时间，单位：毫秒
     */
    @SerializedName("startTimeInMills")
    public long startTimeInMills;
    /**
     * 仅离线广告有效，广告有效性的 截止时间，单位：毫秒
     */
    @SerializedName("endTimeInMills")
    public long endTimeInMills;
}
