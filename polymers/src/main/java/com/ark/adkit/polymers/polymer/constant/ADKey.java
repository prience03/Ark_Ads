package com.ark.adkit.polymers.polymer.constant;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.ark.dict.Utils;

public class ADKey {

    private final boolean mIsOld;

    public ADKey() {
        mIsOld = isOldServer();
    }

    private boolean isOldServer() {
        String pkName = Utils.getContext().getPackageName();
        return TextUtils.equals(pkName, PK.PK_EMOJI)
                || TextUtils.equals(pkName, PK.PK_VIDEO_WP)
                || TextUtils.equals(pkName, PK.PK_ADESK_WP)
                || TextUtils.equals(pkName, PK.PK_LIVE_WP)
                || TextUtils.equals(pkName, PK.PK_LOVE_WP);
    }

    @NonNull
    public String JsonKeyOfAppKey() {
        return mIsOld ? OldServerADConst.AD_APPID_ALL :
                ADConst.AD_APPID_ALL;
    }

    @NonNull
    public String JsonKeyOfSplash() {
        return mIsOld ? OldServerADConst.AD_POSID_SPLASH :
                ADConst.AD_POSID_SPLASH;
    }

    @NonNull
    public String JsonKeyOfList() {
        return mIsOld ? OldServerADConst.AD_POSID_NATIVE_LIST :
                ADConst.AD_POSID_NATIVE_LIST;
    }

    @NonNull
    public String JsonKeyOfDetail() {
        return mIsOld ? OldServerADConst.AD_POSID_NATIVE_DETAIL :
                ADConst.AD_POSID_NATIVE_DETAIL;
    }

    @NonNull
    public String JsonKeyOfSplashSort() {
        return mIsOld ? OldServerADConst.AD_SORT_SPLASH :
                ADConst.AD_SORT_SPLASH;
    }

    @NonNull
    public String JsonKeyOfNativeSort() {
        return mIsOld ? OldServerADConst.AD_SORT_NATIVE :
                ADConst.AD_SORT_NATIVE;
    }

    @NonNull
    public String JsonKeyOfDisableAll() {
        return Const.AD_DISABLE_ALL;
    }

    @NonNull
    public String JsonKeyOfDisableChannel() {
        return Const.AD_DISABLE_CHANNEL;
    }

    @NonNull
    public String JsonKeyOfDisableVersion() {
        return Const.AD_DISABLE_VERSION;
    }

    public String jsonKeyOfVideoSort() {
        return Const.AD_SORT_VIDEO;
    }

    public String jsonKeyOfVideo() {
        return Const.AD_POSID_VIDEO;
    }

    /**
     * 新加字段请使用相同字段，在这里添加
     */
    public interface Const {
        String AD_DISABLE_ALL = "ad_disable_all";
        String AD_DISABLE_VERSION = "ad_disable_version";
        String AD_DISABLE_CHANNEL = "ad_disable_channel";
        String AD_POSID_VIDEO = "ad_posid_video";
        String AD_SORT_VIDEO = "ad_sort_video";
    }

    /**
     * 新后台字段
     */
    public interface ADConst extends Const {
        String AD_APPID_ALL = "pos_ad_appkey_android";
        String AD_POSID_NATIVE_LIST = "pos_ad_native_android";
        String AD_POSID_NATIVE_DETAIL = "ad_posid_native_detail";
        String AD_POSID_SPLASH = "pos_ad_splash_android";
        String AD_SORT_NATIVE = "pos_ad_sort_native_android";
        String AD_SORT_SPLASH = "pos_ad_sort_splash_android";
    }

    /**
     * 老后台字段
     */
    public interface OldServerADConst extends Const {
        String AD_APPID_ALL = "ad_appid_all";
        String AD_POSID_NATIVE_LIST = "ad_posid_native_list";
        String AD_POSID_NATIVE_DETAIL = "ad_posid_native_detail";
        String AD_POSID_SPLASH = "ad_posid_splash";
        String AD_SORT_NATIVE = "ad_sort_native";
        String AD_SORT_SPLASH = "ad_sort_splash_new";
    }

    /**
     * 使用老kv后台app的包名
     */
    public interface PK {

        String PK_EMOJI = "com.emojifair.emoji";
        String PK_VIDEO_WP = "com.novv.resshare";
        String PK_ADESK_WP = "com.androidesk";
        String PK_LOVE_WP = "com.lovebizhi.wallpaper";
        String PK_LIVE_WP = "com.androidesk.livewallpaper";
    }
}
