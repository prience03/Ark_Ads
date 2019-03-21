package com.ark.adkit.polymers.polymer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.androidquery.callback.AQuery2;
import com.ark.adkit.basics.configs.ADStyle;
import com.ark.adkit.basics.data.ADMetaData;
import com.ark.adkit.basics.models.OnNativeListener;
import com.ark.adkit.basics.models.OnSplashImpl;
import com.ark.adkit.polymers.polymer.wrapper.ConfigWrapperImpl;
import com.ark.adkit.polymers.polymer.wrapper.NativeWrapperImpl;
import com.ark.adkit.polymers.polymer.wrapper.SplashWrapperImpl;
import com.ark.dict.Utils;

import java.util.List;
import java.util.Map;

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

    //----------------------------过时方法，以后版本删除 start---------------------------//

    /**
     * 使用getSplashWrapper()调用，后续删除
     *
     * @param activity     上下文
     * @param viewGroup    广告容器
     * @param skipGroup    跳过按钮容器（根容器）
     * @param onSplashImpl 回调
     */
    @Deprecated
    public void loadSplash(@NonNull Activity activity, @NonNull ViewGroup viewGroup,
            @NonNull ViewGroup skipGroup, @NonNull OnSplashImpl onSplashImpl) {
        getSplashWrapper().loadSplash(activity, viewGroup, skipGroup, onSplashImpl);
    }

    /**
     * 使用getConfigWrapper()调用，后续删除
     *
     * @return kv 广告平台id配置
     */
    @NonNull
    @Deprecated
    public Map<String, String> getAppKeyMap() {
        return getConfigWrapper().getAppKeyMap();
    }

    /**
     * 使用getConfigWrapper()调用，后续删除
     *
     * @return kv 广告位id配置
     */
    @NonNull
    @Deprecated
    public Map<String, String> getSubKeyMap(@ADStyle int mStyle) {
        return getConfigWrapper().getSubKeyMap(mStyle);
    }

    /**
     * 使用getConfigWrapper()调用，后续删除
     *
     * @return kv配置
     */
    @NonNull
    @Deprecated
    public Map<String, String> getConfig() {
        return getConfigWrapper().getConfig();
    }

    /**
     * 使用getConfigWrapper()调用，后续删除
     *
     * @return 信息流sort排序
     */
    @NonNull
    @Deprecated
    public List<String> getNativeSort() {
        return getConfigWrapper().getNativeSort();
    }

    /**
     * 使用getConfigWrapper()调用，后续删除
     *
     * @return 开屏sort排序
     */
    @NonNull
    @Deprecated
    public List<String> getSplashSort() {
        return getConfigWrapper().getSplashSort();
    }

    /**
     * 使用getConfigWrapper()调用，后续删除
     *
     * @return true：有广告，false：屏蔽
     */
    @Deprecated
    public boolean hasAd() {
        return getConfigWrapper().hasAd();
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context 上下文
     * @return ADMetaData
     */
    @Nullable
    @Deprecated
    public ADMetaData getListNative(@NonNull Context context) {
        return getNativeWrapper().getListNative(context);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context 上下文
     * @return ADMetaData
     */
    @Nullable
    @Deprecated
    public ADMetaData getDetailNative(@NonNull Context context) {
        return getNativeWrapper().getDetailNative(context);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context     上下文
     * @param adContainer 容器
     */
    @Deprecated
    public void loadNativeView(@NonNull Context context, @NonNull ViewGroup adContainer) {
        getNativeWrapper().loadNativeView(context, adContainer);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context          上下文
     * @param adContainer      容器
     * @param onNativeListener 回调
     */
    @Deprecated
    public void loadNativeView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable OnNativeListener<ADMetaData> onNativeListener) {
        getNativeWrapper().loadNativeView(context, adContainer, onNativeListener);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context     上下文
     * @param adContainer 容器
     */
    @Deprecated
    public void loadSmallNativeView(@NonNull Context context, @NonNull ViewGroup adContainer) {
        getNativeWrapper().loadSmallNativeView(context, adContainer);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context          上下文
     * @param adContainer      容器
     * @param onNativeListener 回调
     */
    @Deprecated
    public void loadSmallNativeView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable OnNativeListener<ADMetaData> onNativeListener) {
        getNativeWrapper().loadSmallNativeView(context, adContainer, onNativeListener);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context     上下文
     * @param adContainer 容器
     */
    @Deprecated
    public void loadBannerView(@NonNull Context context, @NonNull ViewGroup adContainer) {
        getNativeWrapper().loadBannerView(context, adContainer);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context          上下文
     * @param adContainer      容器
     * @param onNativeListener 回调
     */
    @Deprecated
    public void loadBannerView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable OnNativeListener<ADMetaData> onNativeListener) {
        getNativeWrapper().loadBannerView(context, adContainer, onNativeListener);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context          上下文
     * @param adContainer      容器
     * @param nativeAD         数据
     * @param onNativeListener 回调
     */
    @Deprecated
    public void loadNativeView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable ADMetaData nativeAD,
            @Nullable OnNativeListener<ADMetaData> onNativeListener) {
        getNativeWrapper().loadNativeView(context, adContainer, nativeAD, onNativeListener);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context          上下文
     * @param adContainer      容器
     * @param nativeAD         数据
     * @param onNativeListener 回调
     */
    @Deprecated
    public void loadSmallNativeView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable ADMetaData nativeAD,
            @Nullable OnNativeListener<ADMetaData> onNativeListener) {
        getNativeWrapper().loadSmallNativeView(context, adContainer, nativeAD, onNativeListener);
    }

    /**
     * 使用 getNativeWrapper() 调用，后续版本删除
     *
     * @param context          上下文
     * @param adContainer      容器
     * @param nativeAD         数据
     * @param onNativeListener 回调
     */
    @Deprecated
    public void loadBannerView(@NonNull Context context, @NonNull ViewGroup adContainer,
            @Nullable ADMetaData nativeAD,
            @Nullable OnNativeListener<ADMetaData> onNativeListener) {
        getNativeWrapper().loadBannerView(context, adContainer, nativeAD, onNativeListener);
    }
    //----------------------------过时方法，以后版本删除 end---------------------------//

    private static class Holder {

        private static ADManager instance = new ADManager();
    }
}
