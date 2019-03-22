package com.ark.adkit.polymers.self;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.ark.adkit.basics.utils.AppUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class SelfNativeAD {

    private static final String SELF_URL = "http://service.selfad.adesk.com/v2/ad/list";
    private ADListener adListener;
    private int selfStyle;
    private Context mContext;
    private List<SelfDataRef> mList = new ArrayList<>();

    public SelfNativeAD(@NonNull Context context, @SelfADStyle int selfStyle) {
        this.selfStyle = selfStyle;
        this.mContext = context;
    }

    @NonNull
    public SelfNativeAD setListener(@NonNull ADListener adListener) {
        this.adListener = adListener;
        return this;
    }

    public void loadAllADList() {
        if (mList.size() > 0) {
            if (adListener != null) {
                adListener.onAdLoad(mList);
            }
            return;
        }
        Ion.with(mContext)
                .load(AsyncHttpGet.METHOD, SELF_URL)
                .addQuery("code", getCodeByStyle(selfStyle))
                .addQuery("sys", "android")
                .addQuery("bundleid", mContext.getPackageName())
                .addQuery("channel", AppUtils.getChannel(mContext))
                .addQuery("language", AppUtils.getLanguage(mContext))
                .addQuery("appvercode", AppUtils.getVersionCode(mContext) + "")
                .addQuery("sysname", Build.VERSION.RELEASE)
                .addQuery("sysver", Build.VERSION.SDK_INT + "")
                .addQuery("sysmodel", AppUtils.getModel())
                .addQuery("skip", String.valueOf(0))
                .addQuery("limit", String.valueOf(999))
                .setTimeout(5000)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (!TextUtils.isEmpty(result)) {
                            List<SelfDataRef> list = SelfUtils
                                    .getNovaInfo(result);
                            mList.clear();
                            for (SelfDataRef selfDataRef : list) {
                                if (!selfDataRef.isClickOver()
                                        && !selfDataRef.isViewOver()
                                        && !selfDataRef.isInstalled()) {
                                    mList.add(selfDataRef);
                                }
                            }
                            if (adListener != null) {
                                adListener.onAdLoad(mList);
                            }
                        } else {
                            if (adListener != null) {
                                adListener.onAdFailed(-1, "fail");
                            }
                        }
                    }
                });
    }

    private String getCodeByStyle(int selfStyle) {
        if (selfStyle == SelfADStyle.SPLASH) {
            return "splash";
        } else if (selfStyle == SelfADStyle.INTER_RECOMMEND) {
            return "interrecommend";
        }
        return "infolist";
    }

    public interface ADListener {

        void onAdLoad(List<SelfDataRef> dataRefs);

        void onAdFailed(int errorCode, @NonNull String errorMsg);
    }
}
