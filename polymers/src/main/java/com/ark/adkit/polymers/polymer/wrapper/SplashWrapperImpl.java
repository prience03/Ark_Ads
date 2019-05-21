package com.ark.adkit.polymers.polymer.wrapper;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.ark.utils.permissions.PermissionItem;

import java.util.List;

public class SplashWrapperImpl extends SplashWrapper {

    @NonNull
    public SplashWrapperImpl setPermissions(
            @Nullable List<PermissionItem> permissionItemArrayList) {
        this.mPermissionItemArrayList = permissionItemArrayList;
        return this;
    }

    @NonNull
    public SplashWrapperImpl needPermissions(boolean request) {
        this.mRequestPermissions = request;
        return this;
    }

    /**
     * VIP用户不加载广告
     *
     * @param isVip 是否是VIP
     * @return SplashWrapperImpl
     */
    @NonNull
    public SplashWrapperImpl isVipSkip(boolean isVip) {
        this.isVipSkipSplash = isVip;
        return this;
    }

    /**
     * 设置时间值
     *
     * @param delayMills     广告禁用时延迟多久退出开屏 默认3000
     * @param countdownMills 广告倒计时多久退出开屏 默认5000
     * @param interval       倒计时间隔 默认500
     * @return SplashWrapperImpl
     */
    @NonNull
    public SplashWrapperImpl setMills(@IntRange(from = 0, to = 5000) long delayMills,
                                      @IntRange(from = 3000, to = 8000) long countdownMills,
                                      @IntRange(from = 100, to = 1000) long interval) {
        this.delayMills = delayMills;
        this.countDownMills = countdownMills;
        this.interval = interval;
        return this;
    }
}
