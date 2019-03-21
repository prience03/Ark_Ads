package com.ark.adkit.basics.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import com.umeng.analytics.AnalyticsConfig;

import java.io.File;
import java.util.Locale;

public class AppUtils {

    /**
     * 兼容Android7.0的语言获取
     *
     * @param context 上下文
     * @return 格式化后的固定语言缩写
     */
    @NonNull
    public static String getLanguage(@NonNull Context context) {
        try {
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = context.getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = context.getResources().getConfiguration().locale;
            }
            String language = locale.getLanguage() + "-" + locale.getCountry();
            if (language.startsWith("zh-")) {
                if (language.startsWith("zh-CN")) {
                    return "CN";//中文简体
                }
                return "CT";//中文
            }
            if (language.startsWith("fr-")) {
                return "FR";//法语
            }
            if (language.startsWith("de-")) {
                return "GE";//德语
            }
            if (language.startsWith("it-")) {
                return "IT";//意大利语
            }
            if (language.startsWith("ja-")) {
                return "JP";//日语
            }
            if (language.startsWith("ko-")) {
                return "KR";//韩语
            }
            if (language.startsWith("ru-")) {
                return "RU";//俄语
            }
            if (language.startsWith("es-")) {
                return "SP";//西班牙语
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "EN";
    }

    @NonNull
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * @param context 上下文信息
     * @return 获取包信息
     */
    @Nullable
    private static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是当前类的包名，0代表获取版本信息
        try {
            return packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取应用版本号
     *
     * @param context 上下文信息
     * @return 成功返回版本号，失败返回-1
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return -1;
    }

    /**
     * 获取应用版本名
     *
     * @param context 上下文信息
     * @return 成功返回版本名， 失败返回null
     */
    @NonNull
    public static String getVersionName(@NonNull Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return "1.0";
    }

    /**
     * 获取当前应用程序的友盟渠道
     *
     * @param context 上下文信息
     * @return umeng channel
     */
    @NonNull
    public static String getChannel(@Nullable Context context) {
        String str = "default";
        try {
            str = AnalyticsConfig.getChannel(context);
            if (str == null || TextUtils.isEmpty(str) || TextUtils.equals("null", str)) {
                str = "default";
            }
        } catch (NoClassDefFoundError e) {
            //
        }
        return str;
    }

    /**
     * apk 是否已经安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return if true 已存在
     */
    public static boolean checkApkExist(@NonNull Context context, @Nullable String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            if (Build.VERSION.SDK_INT >= 24) {
                ApplicationInfo info = context.getPackageManager()
                        .getApplicationInfo(packageName,
                                PackageManager.MATCH_UNINSTALLED_PACKAGES);
            } else {
                ApplicationInfo info = context.getPackageManager()
                        .getApplicationInfo(packageName,
                                PackageManager.GET_UNINSTALLED_PACKAGES);
            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static void installApk(Context context, File appFile) {
        if (isTargetOver(context, Build.VERSION_CODES.O)
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                startInstallPermissionSettingActivity(context);
                return;
            }
        }
        try {
            Intent intent = getInstallApkIntent(context, appFile);
            if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                context.startActivity(intent);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public static Intent getInstallApkIntent(final Context context, File appFile) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            if (Build.VERSION.SDK_INT >= 24) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri fileUri = FileProvider
                        .getUriForFile(context,
                                context.getApplicationContext().getPackageName() + ".fileprovider",
                                appFile);
                intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(appFile),
                        "application/vnd.android.package-archive");
            }

            return intent;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startInstallPermissionSettingActivity(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isTargetOver(Context context, int ver) {
        int sdkVersion = context.getApplicationInfo().targetSdkVersion;
        return sdkVersion >= ver;
    }
}
