package com.ark.adkit.basics.utils;

import android.util.Log;

public class LogUtils {
    private static final String tag = "logger";
    private static boolean enable = false;

    public static void openDebug() {
        LogUtils.enable = true;
    }

    public static void v(String msg) {
        if (enable)
            Log.d("" + tag, "" + msg);
    }

    public static void d(String msg) {
        if (enable)
            Log.d("" + tag, "" + msg);
    }

    public static void i(String msg) {
        if (enable)
            Log.i("" + tag, "" + msg);
    }

    public static void w(String msg) {
        if (enable)
            Log.w("" + tag, "" + msg);
    }

    public static void e(String msg) {
        if (enable)
            Log.e("" + tag, "" + msg);
    }
}
