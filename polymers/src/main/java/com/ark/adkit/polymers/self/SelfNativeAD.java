package com.ark.adkit.polymers.self;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import com.ark.adkit.basics.utils.AppUtils;
import com.ark.net.urlconn.StringCallback;
import com.yanzhenjie.kalle.Kalle;
import com.yanzhenjie.kalle.Url;
import com.yanzhenjie.kalle.simple.SimpleResponse;

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
        Url.Builder builder = Url.newBuilder(SELF_URL);
        builder.addQuery("code", getCodeByStyle(selfStyle));
        builder.addQuery("sys", "android");
        builder.addQuery("bundleid", mContext.getPackageName());
        builder.addQuery("channel", AppUtils.getChannel(mContext));
        builder.addQuery("language", AppUtils.getLanguage(mContext));
        builder.addQuery("appvercode", AppUtils.getVersionCode(mContext) + "");
        builder.addQuery("sysname", Build.VERSION.RELEASE);
        builder.addQuery("sysver", Build.VERSION.SDK_INT + "");
        builder.addQuery("sysmodel", AppUtils.getModel());
        builder.addQuery("skip", String.valueOf(0));
        builder.addQuery("limit", String.valueOf(999));
        Kalle.get(builder.build())
                .perform(new StringCallback<String>(mContext) {
                    @Override
                    public void onResponse(final SimpleResponse<String, String> response) {
                        if (response.isSucceed()) {
                            List<SelfDataRef> list = SelfUtils
                                    .getNovaInfo(response.succeed());
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
                                adListener.onAdFailed(-1, response.failed());
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
