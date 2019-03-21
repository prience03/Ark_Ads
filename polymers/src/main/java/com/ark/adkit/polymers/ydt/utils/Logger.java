package com.ark.adkit.polymers.ydt.utils;

import android.util.Log;

public class Logger {

    //设为false关闭日志
    public static boolean LOG_ENABLE = false;
    private static final int LOG_I = 0;
    private static final int LOG_V = 1;
    private static final int LOG_D = 2;
    private static final int LOG_W = 3;
    private static final int LOG_E = 4;

    public static void i(String tag, String msg) {
        if (LOG_ENABLE) {
            show(LOG_I, tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (LOG_ENABLE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG_ENABLE) {
            show(LOG_V, tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOG_ENABLE) {
            show(LOG_W, tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG_ENABLE) {
            show(LOG_E, tag, msg);
        }
    }

    private static void show(int type, String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0) {
            return;
        }
        msg = msg.trim();
        int index = 0;
        int segmentSize = 10 * 1024;
        String logContent;
        while (index < msg.length()) {
            if (msg.length() <= index + segmentSize) {
                logContent = msg.substring(index);
            } else {
                logContent = msg.substring(index, segmentSize + index);
            }
            index += segmentSize;
            switch (type) {
                case LOG_I:
                    Log.i(tag, logContent.trim());
                    break;
                case LOG_V:
                    Log.v(tag, msg);
                    break;
                case LOG_D:
                    Log.d(tag, logContent.trim());
                    break;
                case LOG_W:
                    Log.w(tag, msg);
                    break;
                case LOG_E:
                    Log.w(tag, msg);
                    break;
                default:
                    break;
            }
        }
    }
}
