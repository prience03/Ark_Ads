package com.ark.adkit.basics.configs;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({0, 1, 2, 3})
@Retention(RetentionPolicy.SOURCE)
public @interface ADStyle {

    int POS_SPLASH = 0;//开屏
    int POS_STREAM_LIST = 1;//信息流列表
    int POS_STREAM_DETAIL = 2;//信息流详情
    int POS_STREAM_VIDEO = 3;//信息流视频
}
