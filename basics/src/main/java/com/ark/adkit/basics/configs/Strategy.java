package com.ark.adkit.basics.configs;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({0, 1, 2})
@Retention(RetentionPolicy.SOURCE)
public @interface Strategy {
    int order = 0;//顺序      0-->1-->2    0--->1--->2      0-->1-->2    0--->1--->2
    int shuffle = 1;//乱序    0-->2-->1    0--->1--->2      0-->1-->2    0--->1--->2
    int cycle = 2;//循环      0-->1-->2    1--->2--->0      2-->1-->0    0--->1--->2
}