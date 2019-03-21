package com.ark.adkit.basics.models;

import android.support.annotation.NonNull;
import com.ark.adkit.basics.data.ADMetaData;

public interface OnNativeListener<T> {
    void onSuccess(@NonNull T adMetaData);

    void onFailure();
}
