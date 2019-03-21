package com.ark.adkit.polymers.self;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({0, 1, 2})
@Retention(RetentionPolicy.SOURCE)
public @interface SelfADStyle {
    int INFO_LIST = 0;
    int INTER_RECOMMEND = 1;
    int SPLASH = 2;
}
