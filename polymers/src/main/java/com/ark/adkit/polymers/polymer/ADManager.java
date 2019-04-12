package com.ark.adkit.polymers.polymer;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.androidquery.callback.AQuery2;
import com.ark.adkit.polymers.polymer.wrapper.ConfigWrapperImpl;
import com.ark.adkit.polymers.polymer.wrapper.NativeWrapperImpl;
import com.ark.adkit.polymers.polymer.wrapper.SplashWrapperImpl;
import com.ark.dict.Utils;

public class ADManager {

    private final SplashWrapperImpl mSplashWrapper;
    private final NativeWrapperImpl mNativeWrapper;
    private final ConfigWrapperImpl mConfigWrapper;

    private ADManager() {
        mSplashWrapper = new SplashWrapperImpl();
        mNativeWrapper = new NativeWrapperImpl();
        mConfigWrapper = new ConfigWrapperImpl();
    }

    @NonNull
    public static ADManager get() {
        return Holder.instance;
    }

    /**
     * 加载图片
     *
     * @param imageView 图片视图
     * @param imgUrl    图片地址
     */
    public void loadImage(@Nullable ImageView imageView, @Nullable final String imgUrl) {
        if (imgUrl == null || imageView == null) {
            return;
        }
        AQuery2 aQuery = new AQuery2(Utils.getContext());
        aQuery.id(imageView).image(imgUrl, true, true);
    }

    @NonNull
    public SplashWrapperImpl getSplashWrapper() {
        return mSplashWrapper;
    }

    @NonNull
    public NativeWrapperImpl getNativeWrapper() {
        return mNativeWrapper;
    }

    @NonNull
    public ConfigWrapperImpl getConfigWrapper() {
        return mConfigWrapper;
    }

    private static class Holder {

        private static ADManager instance = new ADManager();
    }
}
