package com.ark.adkit.basics.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

public final class FileUtils {

    public static void createDirDirectory(@NonNull String downloadPath) {
        File dirDirectory = new File(downloadPath);
        if (!dirDirectory.exists()) {
            dirDirectory.mkdirs();
        }
    }

    /**
     * 创建一个文件
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return 文件
     */
    @NonNull
    public static File createFile(@NonNull String downloadPath, @NonNull String fileName) {
        return new File(downloadPath, fileName);
    }

    /**
     * 查看一个文件是否存在
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return true | false
     */
    public static boolean fileExists(@NonNull String downloadPath, @NonNull String fileName) {
        return new File(downloadPath, fileName).exists();
    }

    /**
     * 删除一个文件
     *
     * @param downloadPath 路径
     * @param fileName     名字
     * @return true | false
     */
    public static boolean delete(@NonNull String downloadPath, @NonNull String fileName) {
        return new File(downloadPath, fileName).delete();
    }

    @NonNull
    public static File getFileDir(@NonNull Context context, @Nullable String storeDir) {
        File downloadDir;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (storeDir != null) {
                downloadDir = new File(Environment.getExternalStorageDirectory(), storeDir);
            } else {
                downloadDir = new File(context.getExternalCacheDir(), "download");
            }
        } else {
            downloadDir = new File(context.getCacheDir(), "download");
        }
        if (!downloadDir.exists()) {
            //noinspection ResultOfMethodCallIgnored
            downloadDir.mkdirs();
        }
        return downloadDir;
    }
}

