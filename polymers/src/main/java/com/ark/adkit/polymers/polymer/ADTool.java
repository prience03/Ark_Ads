package com.ark.adkit.polymers.polymer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.ark.adkit.basics.configs.Strategy;
import com.ark.adkit.basics.utils.LogUtils;
import com.ark.adkit.polymers.polymer.constant.ADKey;

public class ADTool {

    public static int index = 0;
    private static ADTool sADTool;
    private String mLocalConfig;
    private boolean isDebugMode;
    private boolean isLoadOtherWhenVideoDisable;
    private int strategy;

    private ADTool() {
    }

    public static void initialize(@NonNull Configuration configuration) {
        if (sADTool == null) {
            synchronized (ADTool.class) {
                if (sADTool == null) {
                    sADTool = new ADTool();
                }
            }
            sADTool.init(configuration);
        }
    }

    @NonNull
    public static ADTool getADTool() {
        if (sADTool == null) {
            initialize(new Builder()
                    .setDebugMode(false)
                    .setLocalConfig(null)
                    .build());
        }
        return sADTool;
    }

    public boolean isDebugMode() {
        return isDebugMode;
    }

    public int getStrategy() {
        return strategy;
    }

    public boolean isLoadOtherWhenVideoDisable(){
        return isLoadOtherWhenVideoDisable;
    }

    @NonNull
    public ADManager getManager() {
        return ADManager.get();
    }

    @Nullable
    public String getLocalConfig() {
        return mLocalConfig;
    }

    private void init(@NonNull Configuration configuration) {
        this.isDebugMode = configuration.isDebugMode;
        this.mLocalConfig = configuration.localConfig;
        this.strategy = configuration.strategy;
        this.isLoadOtherWhenVideoDisable = configuration.isLoadOtherWhenVideoDisable;
        if (isDebugMode) {
            LogUtils.openDebug();
        }
    }

    public static class Builder {

        private boolean isDebugMode;
        private String localConfig;
        private int strategy;
        private boolean isLoadOtherWhenVideoDisable;

        public Builder() {
        }

        /**
         * 使用本地json配置
         * @param localConfig json字符串
         * @return Builder
         */
        @NonNull
        public Builder setLocalConfig(@Nullable String localConfig) {
            this.localConfig = localConfig;
            return this;
        }

        /**
         * 设置调试模式，打印日志，显示platform
         * @param isDebugMode 是否调试
         * @return Builder
         */
        @NonNull
        public Builder setDebugMode(boolean isDebugMode) {
            this.isDebugMode = isDebugMode;
            return this;
        }

        /**
         * 设置原生广告排序策略
         * @param strategy 排序策略
         * @return Builder
         */
        @NonNull
        public Builder setStrategy(@Strategy int strategy) {
            this.strategy = strategy;
            return this;
        }

        /**
         * 当加载视频广告为空时，尝试加载横图广告
         * @param loadOtherWhenVideoDisable 是否加载其他
         * @return Builder
         */
        @NonNull
        public Builder setLoadOtherWhenVideoDisable(boolean loadOtherWhenVideoDisable) {
            isLoadOtherWhenVideoDisable = loadOtherWhenVideoDisable;
            return this;
        }

        @NonNull
        public Configuration build() {
            Configuration configuration = new Configuration();
            configuration.isDebugMode = isDebugMode;
            configuration.localConfig = localConfig;
            configuration.strategy = strategy;
            configuration.isLoadOtherWhenVideoDisable = isLoadOtherWhenVideoDisable;
            return configuration;
        }
    }

    private static class Configuration {

        private boolean isDebugMode;
        private String localConfig;
        private int strategy;
        private boolean isLoadOtherWhenVideoDisable;
    }
}
