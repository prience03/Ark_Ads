package com.ark.adkit.polymers.polymer.wrapper;

import android.support.annotation.Nullable;
import com.ark.utils.permissions.PermissionItem;

import java.util.List;

public class SplashWrapperImpl extends SplashWrapper {

    public SplashWrapperImpl setPermissions(
            @Nullable List<PermissionItem> permissionItemArrayList) {
        this.mPermissionItemArrayList = permissionItemArrayList;
        return this;
    }

    public SplashWrapperImpl needPermissions(boolean request) {
        this.mRequestPermissions = request;
        return this;
    }
}
